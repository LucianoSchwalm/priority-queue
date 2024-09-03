//chegada 2 a 5
//saida 3 a 5
//if(actualLength)
//double tempo = a + ((b-a) - lcg.NextRandom())

import java.util.PriorityQueue;

public class App {
    public static void main(String[] args) {
        PriorityQueue<Event> pq = new PriorityQueue<Event>();
        int[] chegada = {2,5};
        int[] saida = {3,5};
        Simulator sim = new Simulator(pq, 5,1,chegada,saida);
        int count = 10000;
        // Gera 10000 números pseudoaleatórios
        for (int i = 0; i < count; i++) {
            Event event = sim.nextEvent();

            if(event.tipo == EventType.CHEGADA ){
                sim.chegada(event);
            } else if(event.tipo == EventType.SAIDA ){
                sim.saida(event);
            }
        }
    }
}
