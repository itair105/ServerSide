package model;

import java.util.List;

public class DijkstraSearcherTest {
    WeightGraphDomain domain;
    DijkstraSearcher searcher;
    @org.junit.Before
    public void setUp() throws Exception {
        domain = new WeightGraphDomain();
        Node node1 = new Node("node1");
        Node node2 = new Node("node2");
        Node node3 = new Node("node3");
        domain.addNode(node1);
        domain.addNode(node2);
        domain.addNode(node3);

        domain.addEdge(new WeightedEdge(node1, node2, 5));
        domain.addEdge(new WeightedEdge(node2, node3, 10));
        domain.addEdge(new WeightedEdge(node1, node3, 90));

        domain.setStartNode(node1);
        domain.setEndNode(node3);

        searcher = new DijkstraSearcher(domain);
    }

    @org.junit.Test
    public void testSearch() throws Exception {
        List<Node> nodes = searcher.search();
        assert (nodes.size() == 3);
        assert (nodes.get(0).getId().equals("node1"));
        assert (nodes.get(1).getId().equals("node2"));
        assert (nodes.get(2).getId().equals("node3"));
    }
}