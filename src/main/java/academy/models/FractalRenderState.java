package academy.models;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class FractalRenderState {
    private AtomicInteger atomicIterationCount;
    private AtomicIntegerArray redColorArray;
    private AtomicIntegerArray greenColorArray;
    private AtomicIntegerArray blueColorArray;
    private AtomicIntegerArray counterArray;

    public FractalRenderState(int width, int height) {
        int square = width * height;
        this.redColorArray = new AtomicIntegerArray(square);
        this.greenColorArray = new AtomicIntegerArray(square);
        this.blueColorArray = new AtomicIntegerArray(square);
        this.counterArray = new AtomicIntegerArray(square);
        this.atomicIterationCount = new AtomicInteger(0);
    }

    public AtomicInteger getAtomicIterationCount() {
        return atomicIterationCount;
    }

    public void setAtomicIterationCount(AtomicInteger atomicIterationCount) {
        this.atomicIterationCount = atomicIterationCount;
    }

    public AtomicIntegerArray getRedColorArray() {
        return redColorArray;
    }

    public void setRedColorArray(AtomicIntegerArray redColorArray) {
        this.redColorArray = redColorArray;
    }

    public AtomicIntegerArray getGreenColorArray() {
        return greenColorArray;
    }

    public void setGreenColorArray(AtomicIntegerArray greenColorArray) {
        this.greenColorArray = greenColorArray;
    }

    public AtomicIntegerArray getBlueColorArray() {
        return blueColorArray;
    }

    public void setBlueColorArray(AtomicIntegerArray blueColorArray) {
        this.blueColorArray = blueColorArray;
    }

    public AtomicIntegerArray getCounterArray() {
        return counterArray;
    }

    public void setCounterArray(AtomicIntegerArray counterArray) {
        this.counterArray = counterArray;
    }
}
