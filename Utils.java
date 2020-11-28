import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static LinkedList<LinkedList<Integer>> permutations(LinkedList<Integer> integers) {
        LinkedList<LinkedList<Integer>> list = new LinkedList<LinkedList<Integer>>();

        int size = integers.size();

        if (size == 1) {
            LinkedList<Integer> linkedList = new LinkedList<Integer>();
            linkedList.addLast(integers.get(0));

            list.addLast(linkedList);
        } else {
            for (int I = 0; I < size; I++) {
                LinkedList<Integer> am1 = new LinkedList<Integer>();
                for (int J = 0; J < size; J++) {
                    if (J != I)
                        am1.add(integers.get(J));
                }

                LinkedList<LinkedList<Integer>> list1 = permutations(am1);

                for (int i = 0; i < list1.size(); i++) {
                    LinkedList<Integer> tempIntegers = list1.get(i);
                    tempIntegers.add(0, integers.get(I));
                    list.add(tempIntegers);
                }
            }
        }

        return list;
    }

    public static List<Integer> fillPath(ArrayList<Integer> previus, int start, int end ) {
        LinkedList<Integer> shortestPath = new LinkedList<Integer>();

        int tempEnd = end;
        if (tempEnd == start || previus.get(tempEnd) >= 0) {
            while (tempEnd >= 0) {
                shortestPath.addFirst(tempEnd);
                tempEnd = previus.get(tempEnd);
            }
        }

        return shortestPath;
    }


}
