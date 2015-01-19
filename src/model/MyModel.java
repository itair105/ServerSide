package model;

import common.Observer;
import common.Utils;

import java.util.*;

public class MyModel implements Model {
    private GraphDomain domain;
    private AbstractSearcher searcher;
    private Solution solution;
    Set<Observer> listeners = new HashSet<Observer>();
    Map<String, Solution> solutionMap = new HashMap<String, Solution>();

    @Override
    public Map<String, Solution> getSolutionMap() {
        return solutionMap;
    }


    @Override
    public void selectDomain(String domainName) {
        if (domainName.equals("weightedGraph")) {
            domain = createWeightedGraph();
        } else if (domainName.equals("weightlessGraph")) {
            domain = createWeightLessGraph();
        } else {
            throw new IllegalArgumentException("no such domain name");
        }

        System.out.println(domain.hashCode());

        if (searcher != null) {
            searcher.setDomain(domain);
        }
    }

    @Override
    public void selectAlgorithm(String algorithmName) {
        if (algorithmName.equals("bfs")) {
            searcher = new BFSSearcher(domain);
        } else if (algorithmName.equals("dijkstra")) {
            searcher = new DijkstraSearcher((WeightGraphDomain) domain);
        }
    }

    @Override
    public void solveDomain() {
        long time = System.currentTimeMillis();

        int hash = domain.hashCode() + searcher.getClass().getSimpleName().hashCode();
        String hashStr = String.valueOf(hash);
        if (solutionMap.containsKey(hashStr)) {
            solution = solutionMap.get(hashStr);
        } else {
            solution = new Solution(searcher.search(), hashStr);
            solutionMap.put(hashStr, solution);
        }

        long time2 = System.currentTimeMillis();
        long timePast = time2 - time;
        System.out.println("solution took  " + timePast + " ms");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public GraphDomain getDomain() {
        return domain;
    }

    @Override
    public void addObserver(Observer observer) {
        listeners.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        listeners.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer listener : listeners) {
            listener.update(this);
        }

    }

    private GraphDomain createWeightLessGraph() {
        WeightLessGraphDomain weightLessGraphDomain = new WeightLessGraphDomain();
        ArrayList<Node> nodes = new ArrayList<Node>();
        int numberOfNodes = Utils.getRandom(15) + 2;
        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new Node("node" + i);
            nodes.add(node);
            weightLessGraphDomain.addNode(node);
        }
        int numberOfEdges = numberOfNodes * 3;
        for (int i = 0; i < numberOfEdges; i++) {
            int node1Index = -1;
            int node2Index = -1;
            while (node1Index == node2Index) {
                node1Index = Utils.getRandom(numberOfNodes) - 1;
                node2Index = Utils.getRandom(numberOfNodes) - 1;
            }

            Node node1 = nodes.get(node1Index);
            Node node2 = nodes.get(node2Index);
            WeightLessEdge edge = new WeightLessEdge(node1, node2);
            weightLessGraphDomain.addEdge(edge);
        }

        int startNodeIndex = Utils.getRandom(numberOfNodes) - 1;
        int endNodeIndex = startNodeIndex;
        while (startNodeIndex == endNodeIndex) {
            endNodeIndex = Utils.getRandom(numberOfNodes) - 1;
        }
        Node start = nodes.get(startNodeIndex);
        Node end = nodes.get(endNodeIndex);

        weightLessGraphDomain.setStartNode(start);
        weightLessGraphDomain.setEndNode(end);

        return weightLessGraphDomain;
    }

    private GraphDomain createWeightedGraph() {
        WeightGraphDomain weightGraphDomain = new WeightGraphDomain();
        ArrayList<Node> nodes = new ArrayList<Node>();
        int numberOfNodes = Utils.getRandom(15) + 2;
        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new Node("node" + i);
            nodes.add(node);
            weightGraphDomain.addNode(node);
        }
        int numberOfEdges = numberOfNodes * 2;
        for (int i = 0; i < numberOfEdges; i++) {
            int node1Index = -1;
            int node2Index = -1;
            int weight = Utils.getRandom(numberOfNodes) - 1;
            while (node1Index == node2Index) {
                node1Index = Utils.getRandom(numberOfNodes) - 1;
                node2Index = Utils.getRandom(numberOfNodes) - 1;
                Node node1 = nodes.get(node1Index);
                Node node2 = nodes.get(node2Index);
                WeightedEdge edge = new WeightedEdge(node1, node2, weight);
                weightGraphDomain.addEdge(edge);
            }
        }
        int startNodeIndex = Utils.getRandom(numberOfNodes) - 1;
        int endNodeIndex = startNodeIndex;
        while (endNodeIndex == startNodeIndex) {
            endNodeIndex = Utils.getRandom(numberOfNodes) - 1;
        }

        Node start = nodes.get(startNodeIndex);
        Node end = nodes.get(endNodeIndex);

            weightGraphDomain.setStartNode(start);
            weightGraphDomain.setEndNode(end);

            return weightGraphDomain;
    }
}
