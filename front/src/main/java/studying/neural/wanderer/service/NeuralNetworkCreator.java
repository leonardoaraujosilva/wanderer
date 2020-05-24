package studying.neural.wanderer.service;

import studying.neural.wanderer.NeuralNetwork;

public class NeuralNetworkCreator {

    public NeuralNetwork create(NeuralNetwork neuralNetwork, double range, int max, int index) {
        if (neuralNetwork == null)
            return create();

        // initialize with random weights
        var newNeuralNetwork = new NeuralNetwork(3);
        var inputLayerIds = neuralNetwork.getLayer(0).keySet();
        var hiddenLayerIds = neuralNetwork.getLayer(1).keySet();
        var outputLayerIds = neuralNetwork.getLayer(2).keySet();

        // number of combinations (weights)
        int combinations = inputLayerIds.size() * hiddenLayerIds.size() + hiddenLayerIds.size() * outputLayerIds.size();
        double value = range * combinations;
        double slice = value / max;

        for(var each : inputLayerIds)
            newNeuralNetwork.createNeuron(each, 0);
        for(var each : hiddenLayerIds)
            newNeuralNetwork.createNeuron(each, 1);
        for(var each : outputLayerIds)
            newNeuralNetwork.createNeuron(each, 2);

        var combCount = 1;
        for (var eachInput : inputLayerIds) {
            for (var eachHidden : hiddenLayerIds) {
                for (var eachOutput : outputLayerIds) {
                    newNeuralNetwork.createConnection(1, eachHidden, eachOutput, balance(index, slice, range, combinations, max, combCount));
                    combCount++;
                }
                newNeuralNetwork.createConnection(0, eachInput, eachHidden, balance(index, slice, range, combinations, max, combCount));//balance(neuralNetwork.getLayer(0).get(eachInput).getWeight(eachHidden)));
                combCount++;
            }
        }

        return newNeuralNetwork;
    }

    private double balance(int index, double slice, double range, int connections, int max, int conIndex) {
        var multiplicador = index / connections;
        var resto = index % connections - conIndex;
        var value = resto <= 0? 0 : multiplicador * slice;
        double sum = value - range / 2;
        return Math.max(-1000, Math.min(1000, sum));
    }

    public NeuralNetwork create() {
        var neuralNetwork = new NeuralNetwork(3);

        neuralNetwork.createNeuron("Sensor 01", 0);
        neuralNetwork.createNeuron("Sensor 02", 0);
        neuralNetwork.createNeuron("Sensor 03", 0);
//        neuralNetwork.createNeuron("Sensor 04", 0);
//        neuralNetwork.createNeuron("Sensor 05", 0);
//        neuralNetwork.createNeuron("distanceToFeed", 0);
        neuralNetwork.createNeuron("currentDirectionX", 0);
        neuralNetwork.createNeuron("currentDirectionY", 0);
        neuralNetwork.createNeuron("leftCounter", 0);
        neuralNetwork.createNeuron("rightCounter", 0);
//        neuralNetwork.createNeuron("lifeTime", 0);
        neuralNetwork.createNeuron("feedX", 0);
        neuralNetwork.createNeuron("feedY", 0);

        neuralNetwork.createNeuron("Hidden 01", 1);
        neuralNetwork.createNeuron("Hidden 02", 1);
        neuralNetwork.createNeuron("Hidden 03", 1);
        neuralNetwork.createNeuron("Hidden 04", 1);
        neuralNetwork.createNeuron("Hidden 05", 1);
        neuralNetwork.createNeuron("Hidden 06", 1);
        neuralNetwork.createNeuron("Hidden 07", 1);
        neuralNetwork.createNeuron("Hidden 08", 1);
        neuralNetwork.createNeuron("Hidden 09", 1);
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
