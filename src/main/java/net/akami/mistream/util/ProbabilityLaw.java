package net.akami.mistream.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.Function;

public class ProbabilityLaw<T> {

    private final Map<T, Float> events;
    private final Random random;
    private float totalWeight;

    public ProbabilityLaw() {
        this.events = new HashMap<>();
        this.random = new Random();
        this.totalWeight = 0;
    }

    public static <T> ProbabilityLaw<T> of(List<T> elements, Function<T, Float> func) {
        ProbabilityLaw<T> law = new ProbabilityLaw<>();
        for(T element : elements) {
            law.add(element, func.apply(element));
        }
        return law;
    }

    public void add(T element, float weight) {
        events.put(element, weight);
        totalWeight += weight;
    }

    public void clear() {
        events.clear();
        totalWeight = 0;
    }

    public void remove(T element) {
        totalWeight -= events.get(element);
        events.remove(element);
    }

    public T draw() {
        if(events.size() == 0) {
            return null;
        }

        float current = 0;
        float randValue = random.nextFloat() * totalWeight;
        T previous = null;
        for(Entry<T, Float> event : events.entrySet()) {
            if(current > randValue) {
                return previous;
            }
            previous = event.getKey();
            current += event.getValue();
        }
        return previous;
    }
}
