
#!/usr/bin/env bash

set -uo pipefail

# Количество повторений каждого эксперимента
REPEATS="${REPEATS:-5}"

# Определение корневой директории проекта
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

RESULT_DIR="$PROJECT_DIR/benchmark-results"
LOG_DIR="$RESULT_DIR/logs"
CSV_FILE="$RESULT_DIR/results.csv"

mkdir -p "$LOG_DIR"

# Определение системы сборки
if [[ -f "$PROJECT_DIR/mvnw" ]]; then
    BUILD_TOOL="maven"
elif [[ -f "$PROJECT_DIR/gradlew" ]]; then
    BUILD_TOOL="gradle"
elif [[ -f "$PROJECT_DIR/pom.xml" ]]; then
    BUILD_TOOL="maven"
elif [[ -f "$PROJECT_DIR/build.gradle" || -f "$PROJECT_DIR/build.gradle.kts" ]]; then
    BUILD_TOOL="gradle"
else
    echo "Ошибка: не удалось определить систему сборки."
    exit 1
fi

echo "=========================================="
echo "       FRACTAL FLAME BENCHMARK"
echo "=========================================="
echo "Система сборки: $BUILD_TOOL"
echo "Количество повторений: $REPEATS"
echo "Результаты: $RESULT_DIR"
echo "=========================================="

# Подготовка CSV
echo "experiment,value,run,time_seconds" > "$CSV_FILE"

# Функция запуска приложения
run_app() {
    local args="$*"

    if [[ "$BUILD_TOOL" == "maven" ]]; then
        if [[ -f "$PROJECT_DIR/mvnw" ]]; then
            (cd "$PROJECT_DIR" && ./mvnw -q \
                -Dexec.mainClass=academy.Application \
                -Dexec.args="$args" \
                exec:java)
        else
            (cd "$PROJECT_DIR" && mvn -q \
                -Dexec.mainClass=academy.Application \
                -Dexec.args="$args" \
                exec:java)
        fi
    else
        if [[ -f "$PROJECT_DIR/gradlew" ]]; then
            (cd "$PROJECT_DIR" && ./gradlew -q run --args="$args")
        else
            (cd "$PROJECT_DIR" && gradle -q run --args="$args")
        fi
    fi
}

# Функция проведения одного эксперимента
run_experiment() {
    local experiment="$1"
    local value="$2"
    shift 2

    local args=("$@")

    echo ""
    echo "------------------------------------------"
    echo "Эксперимент: $experiment"
    echo "Значение: $value"
    echo "------------------------------------------"

    for ((run = 1; run <= REPEATS; run++)); do
        local log_file="$LOG_DIR/${experiment}_${value}_run${run}.log"

        echo ""
        echo "Запуск $run/$REPEATS"

        if run_app "${args[@]}" 2>&1 | tee "$log_file"; then
            local time

            time=$(grep -Eo \
                'Время генерации фрактала: [0-9]+([.,][0-9]+)? сек\.' \
                "$log_file" |
                tail -n 1 |
                sed -E 's/Время генерации фрактала: (.*) сек\./\1/')

            if [[ -n "$time" ]]; then
                # Приведение десятичного разделителя к точке
                time="${time/,/.}"

                echo "$experiment,$value,$run,$time" >> "$CSV_FILE"

                echo "Время генерации: $time сек."
            else
                echo "$experiment,$value,$run,NOT_FOUND" >> "$CSV_FILE"
                echo "Не удалось найти время генерации в логе."
            fi
        else
            echo "$experiment,$value,$run,ERROR" >> "$CSV_FILE"
            echo "Ошибка выполнения эксперимента."
        fi
    done
}

# Общие параметры
COMMON=(
    --width=1920
    --height=1080
    --seed=5.1234
    --iteration-count=2500000
    --threads=1
    --functions=spherical:1.0
    --gamma-correction=2.2
    --symmetry-level=1
)

echo ""
echo "========== ТАБЛИЦА 1: ПОТОКИ =========="

for threads in 1 2 4 8 16; do
    run_experiment "threads" "$threads" \
        --width=1920 --height=1080 \
        --seed=5.1234 \
        --iteration-count=2500000 \
        --threads="$threads" \
        --functions=spherical:1.0 \
        --gamma-correction=2.2 \
        --symmetry-level=1
done

echo ""
echo "========== ТАБЛИЦА 2: ИТЕРАЦИИ =========="

for iterations in 100000 500000 1000000 2500000 5000000; do
    run_experiment "iterations" "$iterations" \
        --width=1920 --height=1080 \
        --seed=5.1234 \
        --iteration-count="$iterations" \
        --threads=1 \
        --functions=spherical:1.0 \
        --gamma-correction=2.2 \
        --symmetry-level=1
done

echo ""
echo "========== ТАБЛИЦА 3: РАЗРЕШЕНИЕ =========="

run_experiment "resolution" "640x480" \
    --width=640 --height=480 --seed=5.1234 \
    --iteration-count=2500000 --threads=1 \
    --functions=spherical:1.0 --gamma-correction=2.2 \
    --symmetry-level=1

run_experiment "resolution" "1280x720" \
    --width=1280 --height=720 --seed=5.1234 \
    --iteration-count=2500000 --threads=1 \
    --functions=spherical:1.0 --gamma-correction=2.2 \
    --symmetry-level=1

run_experiment "resolution" "1920x1080" \
    --width=1920 --height=1080 --seed=5.1234 \
    --iteration-count=2500000 --threads=1 \
    --functions=spherical:1.0 --gamma-correction=2.2 \
    --symmetry-level=1

run_experiment "resolution" "2560x1440" \
    --width=2560 --height=1440 --seed=5.1234 \
    --iteration-count=2500000 --threads=1 \
    --functions=spherical:1.0 --gamma-correction=2.2 \
    --symmetry-level=1

run_experiment "resolution" "3840x2160" \
    --width=3840 --height=2160 --seed=5.1234 \
    --iteration-count=2500000 --threads=1 \
    --functions=spherical:1.0 --gamma-correction=2.2 \
    --symmetry-level=1

echo ""
echo "========== ТАБЛИЦА 4: GAMMA =========="

for gamma in 1.0 1.5 2.0 2.2 2.5 3.0; do
    run_experiment "gamma" "$gamma" \
        --width=1920 --height=1080 \
        --seed=5.1234 \
        --iteration-count=2500000 \
        --threads=1 \
        --functions=spherical:1.0 \
        --gamma-correction="$gamma" \
        --symmetry-level=1
done

echo ""
echo "========== ТАБЛИЦА 5: СИММЕТРИЯ =========="

for symmetry in 1 2 4 8 16; do
    run_experiment "symmetry" "$symmetry" \
        --width=1920 --height=1080 \
        --seed=5.1234 \
        --iteration-count=2500000 \
        --threads=1 \
        --functions=spherical:1.0 \
        --gamma-correction=2.2 \
        --symmetry-level="$symmetry"
done

echo ""
echo "========== ТАБЛИЦА 6: ФУНКЦИИ =========="

for function in linear sinusoidal spherical swirl horseshoe polar; do
    run_experiment "function" "$function" \
        --width=1920 --height=1080 \
        --seed=5.1234 \
        --iteration-count=2500000 \
        --threads=1 \
        --functions="$function:1.0" \
        --gamma-correction=2.2 \
        --symmetry-level=1
done

echo ""
echo "=========================================="
echo "          BENCHMARK COMPLETED"
echo "=========================================="
echo "CSV с результатами: $CSV_FILE"
echo "Логи запусков: $LOG_DIR"
echo "Количество запусков: $((32 * REPEATS))"
echo "=========================================="
