package model;

import java.util.List;

import static org.junit.Assert.*;

public class BFSSearcherTest {
    WeightLessGraphDomain domain;
    BFSSearcher bfsSearcher;
    @org.junit.Before
    public void setUp() throws Exception {
        domain = new WeightLessGraphDomain();
        Node node1 = new Node("node1");
        Node node2 = new Node("node2");
        Node node3 = new Node("node3");
        domain.addNode(node1);
        domain.addNode(node2);
        domain.addNode(node3);

        domain.addEdge(new WeightLessEdge(node1, node2));
        domain.addEdge(new WeightLessEdge(node2, node3));

        domain.setStartNode(node1);
        domain.setEndNode(node3);

        bfsSearcher = new BFSSearcher(domain);
    }

    @org.junit.Test
    public void testSearch() throws Exception {
        List<Node> nodes = bfsSearcher.search();
        assert (nodes.get(0).getId().equals("node1"));
        assert (nodes.get(1).getId().equals("node2"));
        assert (nodes.get(2).getId().equals("node3"));
    }
}