import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulator {
    private PriorityQueue<Event> minHeap;
    private ArrayList<Event> losses;
    private LinearCongruentialGenerator lcg;
    private int capacity;
    private int servers;
    private double tempoGlobal;
    private int[] timeChegada;
    private int[] timeSaida;

    public Simulator(PriorityQueue<Event> minHeap, int capacity, int servers,int[] timeChegada, int[] timeSaida){
        this.minHeap = minHeap;
        this.capacity = capacity;
        this.servers = servers;
        this.tempoGlobal = 0;
        this.losses = new ArrayList<Event>();
        this.lcg = new LinearCongruentialGenerator();
        this.timeChegada = timeChegada;
        this.timeSaida = timeSaida;
    }
    public void chegada(Event event){
        acumulaTempo(event);
        if(minHeap.size() < capacity){
            minHeap.add(event);
            System.out.println("ADICIONEI na fila");
            if(minHeap.size() <= servers) {
                outEvent();
            }
        }
        else{
            loss(event);
        }
    }
    
    public void saida(Event event){
        acumulaTempo(event);
        minHeap.poll();
        System.out.println("TIREI da fila");
        if(minHeap.size() >= servers){
            outEvent();
        }
    }

    public Event nextEvent(){
        if(minHeap.size() == 0 && tempoGlobal == 0){
            System.out.println("cria primeiro evento de CHEGADA");
            return new Event(tempoGlobal+2, EventType.CHEGADA);
        }
        System.out.println("cria evento de CHEGADA");
        double tempoEvento = timeChegada[0] + ((timeChegada[1] - timeChegada[0]) * lcg.NextRandom());
        return new Event(tempoGlobal+tempoEvento, EventType.CHEGADA);
    }

    public Event outEvent(){
        System.out.println("cria evento de SAIDA");
        double tempoEvento = timeSaida[0] + ((timeSaida[1] - timeSaida[0]) * lcg.NextRandom());
        return new Event(tempoGlobal+tempoEvento, EventType.SAIDA);
    }

    public void acumulaTempo(Event event){
        this.tempoGlobal += event.tempo;
    }

    public void loss(Event event){
        losses.add(event);
    }
}
