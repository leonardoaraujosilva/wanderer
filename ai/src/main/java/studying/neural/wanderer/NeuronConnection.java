package studying.neural.wanderer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NeuronConnection implements Serializable {

    private Neuron input;
    private Neuron output;
    private double weight;

}
