import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulator {
    private PriorityQueue<Event> minHeap;
    private PriorityQueue<Event> waiting;
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
        this.waiting = new PriorityQueue<Event>();
    }

    public void chegada(Event event){
        acumulaTempo(event);
        if(waiting.size() < capacity){
            if(minHeap.size() <= servers) {
                minHeap.poll();
                outEvent(event);
                System.out.println("RETIREI UMA CHEGADA");
            } else{
                waiting.add(event);
                minHeap.poll();
            }
        }
        else{
            loss(event);
        }
    }
    
    public void saida(Event event){
        acumulaTempo(event);
        minHeap.poll();
        System.out.println("RETIREI UMA SAIDA");
        if(minHeap.size() >= servers){
            waiting.element().tempo = tempoGlobal;
            outEvent(waiting.element());
        }
    }

    public Event nextEvent(){
        Event event;
        if(minHeap.size() == 0 && tempoGlobal == 0){
            System.out.println("cria primeiro evento de CHEGADA");
            event = new Event(tempoGlobal+2, EventType.CHEGADA);
            minHeap.add(event);
            return event;
        }

        System.out.println("cria evento de CHEGADA");
        double tempoEvento = timeChegada[0] + ((timeChegada[1] - timeChegada[0]) * lcg.NextRandom());
        event = new Event(tempoGlobal+tempoEvento, EventType.CHEGADA);
        minHeap.add(event);

        return minHeap.element();
    }

    public Event outEvent(Event eventChegada){
        System.out.println("cria evento de SAIDA");
        double tempoEvento = timeSaida[0] + ((timeSaida[1] - timeSaida[0]) * lcg.NextRandom());
        Event eventSaida = new Event(eventChegada.tempo+tempoEvento, EventType.SAIDA);
        minHeap.add(eventSaida);
        return eventSaida;
    }

    public void acumulaTempo(Event event){
        this.tempoGlobal = event.tempo;
        System.out.println("tempo global:" + tempoGlobal);
    }

    public void loss(Event event){
        losses.add(event);
        System.out.println("perdeu hahaha");
    }
}
