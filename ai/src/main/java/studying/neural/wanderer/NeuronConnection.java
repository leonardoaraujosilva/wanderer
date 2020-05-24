package studying.neural.wanderer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NeuronConnection {

    private Neuron input;
    private Neuron output;
    private double weight;

}
