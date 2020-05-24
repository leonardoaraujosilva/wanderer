package studying.neural.wanderer;

import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ToString
public class NeuralNetwork implements Serializable {

     private ArrayList<Map<String, Neuron>> network = new ArrayList<>();

     public NeuralNetwork(int layers) {
          for(var i = 0; i < layers; i++)
               network.add(new HashMap<>());
     }

     public void createNeuron(String id, int layer) {
          network.get(layer).put(id, new Neuron(id));
     }

     public void createConnection(int inputLayer, String inputId, String outputId, double weight) {
          var inputNeuron = network.get(inputLayer).get(inputId);
          var outputNeuron = network.get(inputLayer + 1).get(outputId);
          inputNeuron.addOutput(outputNeuron, weight);
     }

     public Map<String, Neuron> getLayer(int layer) {
          return network.get(layer);
     }

     public Map<String, Double> calculate(Map<String, Double> input) {

          var response = input;
          // each layer
          for(var layer = 0; layer < network.size() - 1; layer++) {

               input = response;
               response = new HashMap<>();
               // each neuron on current output layer
               var currentOutputLayer = network.get(layer + 1);
               for(var outputKey : currentOutputLayer.keySet()) {

                    // each neuron on current input layer
                    var currentInputLayer = network.get(layer);
                    var sum = 0.0;
                    for (var inputKey : currentInputLayer.keySet())
                         sum += input.getOrDefault(inputKey, 0.0) * currentInputLayer.get(inputKey).getWeight(outputKey);

                    if(sum < 0)
                         sum = 0;

                    response.put(outputKey, sum);
               }
          }

          return response;
     }
}
