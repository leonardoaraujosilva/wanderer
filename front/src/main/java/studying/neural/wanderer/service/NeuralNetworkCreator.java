package studying.neural.wanderer.service;

import studying.neural.wanderer.NeuralNetwork;

public class NeuralNetworkCreator {

    public NeuralNetwork create(NeuralNetwork neuralNetwork) {
        if (neuralNetwork == null)
            return create();

        // initialize with random weights
        var inputLayerIds = neuralNetwork.getLayer(0).keySet();
        var hiddenLayerIds = neuralNetwork.getLayer(1).keySet();
        var outputLayerIds = neuralNetwork.getLayer(2).keySet();

        for (var eachInput : inputLayerIds) {
            for (var eachHidden : hiddenLayerIds) {
                for (var eachOutput : outputLayerIds) {
                    neuralNetwork.createConnection(1, eachHidden, eachOutput, Math.max(0, neuralNetwork.getLayer(1).get(eachHidden).getWeight(eachOutput) + ((Math.random() * 100 > 50) ? Math.random() * 2001 - 1001 : Math.random() * 1001.0 - 501.0)));
                }
                neuralNetwork.createConnection(0, eachInput, eachHidden, Math.max(0, neuralNetwork.getLayer(0).get(eachInput).getWeight(eachHidden) + ((Math.random() * 100 > 50) ? Math.random() * 2001 - 1001 : Math.random() * 1001.0 - 501.0)));
            }
        }

        return neuralNetwork;
    }

    public NeuralNetwork create() {
        var neuralNetwork = new NeuralNetwork(3);

        neuralNetwork.createNeuron("Sensor 01", 0);
        neuralNetwork.createNeuron("Sensor 02", 0);
        neuralNetwork.createNeuron("Sensor 03", 0);
        neuralNetwork.createNeuron("Sensor 04", 0);
        neuralNetwork.createNeuron("Sensor 05", 0);
        neuralNetwork.createNeuron("distanceToFeed", 0);
        neuralNetwork.createNeuron("lifeTime", 0);

        neuralNetwork.createNeuron("Hidden 01", 1);
        neuralNetwork.createNeuron("Hidden 02", 1);
//        neuralNetwork.createNeuron("Hidden 03", 1);
//        neuralNetwork.createNeuron("Hidden 04", 1);
//        neuralNetwork.createNeuron("Hidden 05", 1);
//        neuralNetwork.createNeuron("Hidden 06", 1);
//        neuralNetwork.createNeuron("Hidden 07", 1);
//        neuralNetwork.createNeuron("Hidden 08", 1);
//        neuralNetwork.createNeuron("Hidden 09", 1);
//        neuralNetwork.createNeuron("Hidden 10", 1);
//        neuralNetwork.createNeuron("Hidden 11", 1);
//        neuralNetwork.createNeuron("Hidden 12", 1);
//        neuralNetwork.createNeuron("Hidden 13", 1);
//        neuralNetwork.createNeuron("Hidden 14", 1);

        neuralNetwork.createNeuron("turnLeft", 2);
        neuralNetwork.createNeuron("turnRight", 2);

        // initialize with random weights
        var inputLayerIds = neuralNetwork.getLayer(0).keySet();
        var hiddenLayerIds = neuralNetwork.getLayer(1).keySet();
        var outputLayerIds = neuralNetwork.getLayer(2).keySet();

        for (var eachInput : inputLayerIds) {
            for (var eachHidden : hiddenLayerIds) {
                for (var eachOutput : outputLayerIds) {
                    neuralNetwork.createConnection(1, eachHidden, eachOutput, Math.random() * 2001 - 1001);
                }
                neuralNetwork.createConnection(0, eachInput, eachHidden, Math.random() * 2001 - 1001);
            }
        }

        return neuralNetwork;
    }

}
