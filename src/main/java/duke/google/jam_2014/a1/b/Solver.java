package duke.google.jam_2014.a1.b;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Created by andrew on 26.04.14.
 */
public class Solver {
    public static void main(String[] args) throws IOException {
        new Solver().solve("C:\\Andrew\\develop\\google-jam\\2014\\1a\\b\\test.out", "C:\\Andrew\\develop\\google-jam\\2014\\1a\\b\\res.out");
    }

    private Map<Integer, List<Integer>> map;
    private Map<Integer, Node> nodeMap;
    private TreeSet<Integer> candidateToDelete;

    public void solve(String in, String out) throws IOException {
        List<String> result = new LinkedList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(in)))) {
            int size = Integer.parseInt(br.readLine());
            for (int i = 1; i <= size; i++) {
                map = new HashMap<>();
                nodeMap = new HashMap<>();
                candidateToDelete = new TreeSet<>();
                int count = Integer.parseInt(br.readLine());
                for (int j = 0; j < count - 1; j++) {
                    String s = br.readLine();
                    String[] split = s.split(" ");
                    checkMap(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                    checkMap(Integer.parseInt(split[1]), Integer.parseInt(split[0]));
                }
                result.add(solveItem(i));
            }
        }
        Files.write(Paths.get(out), result, Charset.defaultCharset(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void checkMap(Integer k, Integer v) {
        List<Integer> list = map.get(k);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(v);
        map.put(k, list);
    }


    private TreeSet<Double> parseSet(String s) {
        TreeSet<Double> res = new TreeSet<>();
        String[] split = s.split(" ");
        for (String s1 : split) {
            res.add(Double.parseDouble(s1));
        }
        return res;
    }

    private String solveItem(int i) {
        int result = 0;
        constructTress();
        for (Map.Entry<Integer, Node> entry : nodeMap.entrySet()) {
            int empty = 0;
            if (entry.getValue().right == null) {
                empty++;
            }
            if (entry.getValue().left == null) {
                empty++;
            }
            if (empty == 1) {
                candidateToDelete.add(entry.getKey());
            }
        }

        while (!candidateToDelete.isEmpty()) {
            Integer first = candidateToDelete.pollFirst();
            Node node = nodeMap.get(first);
            if (node != null) {
                counter = 0;
                countChildsAndDeleteNodes(node);
                result += counter;
                node.left = null;
                node.right = null;
            }

        }

        return "Case #" + i + ": " + result;
    }

    private int counter;

    private void countChildsAndDeleteNodes(Node node) {
        if (node.left != null) {
            counter++;
            countChildsAndDeleteNodes(node.left);
        }
        if (node.right != null) {
            counter++;
            countChildsAndDeleteNodes(node.right);
        }
        nodeMap.remove(node.val);
    }


    private void constructTress() {
        LinkedList<Integer> candidates = new LinkedList<>();
        TreeSet<Integer> candidatesSet = new TreeSet<>();
        Set<Integer> considered = new TreeSet<>();
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            if (entry.getValue().size() == 1) {
                candidates.add(entry.getKey());
                candidatesSet.add(entry.getKey());
            }
        }
        while (!candidates.isEmpty()) {

            Integer c = candidates.pollFirst();
            candidatesSet.pollFirst();
            Node child = nodeMap.get(c);
            if (child == null) {
                child = new Node();
                child.val = c;
            }

            List<Integer> list = map.get(c);
            Integer parVal = null;
            for (Integer integer : list) {
                if (!considered.contains(integer) && !candidatesSet.contains(integer)) {
                    candidates.add(integer);
                    candidatesSet.add(integer);
                }
                if (!considered.contains(integer)) {
                    parVal = integer;
                }
            }
            if (parVal != null) {
                Node par = nodeMap.get(parVal);
                if (par == null) {
                    par = new Node();
                    par.val = parVal;
                    par.left = child;
                    // candidateToDelete.add(parVal);
                } else {
                    // candidateToDelete.remove(parVal);
                    par.right = child;
                }
                child.par = par;
                nodeMap.put(parVal, par);
                nodeMap.put(c, child);
            }
            considered.add(c);

        }

    }


    private static class Node {
        Node left;
        Node right;
        Node par;
        int val;
    }
}
