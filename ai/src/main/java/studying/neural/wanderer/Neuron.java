package studying.neural.wanderer;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

public class Neuron implements Serializable {

    @Getter
    private String id;

    private HashMap<String, NeuronConnection> inputConnectionList = new HashMap<>();

    private HashMap<String, NeuronConnection> outputConnectionList = new HashMap<>();

    public Neuron(String id) {
        this.id = id;
    }

    public void addOutput(Neuron output, double weight) {
        var connection = new NeuronConnection(this, output, weight);
        outputConnectionList.put(output.getId(), connection);
        output.inputConnectionList.put(output.getId(), connection);
    }

    public double getWeight(String outputId) {
        return outputConnectionList.get(outputId).getWeight();
    }

    @Override
    public String toString() {
        var s = id;
        for(var each : outputConnectionList.entrySet())
            s = s + "[" + each.getKey() + "=" + each.getValue().getWeight() +"],";
        return s;
    }

}
