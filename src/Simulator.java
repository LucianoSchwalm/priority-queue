import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulator {
    private PriorityQueue<Event> chegada;
    private PriorityQueue<Event> saida;
    private PriorityQueue<Event> filaPq;
    private ArrayList<Event> losses;
    private LinearCongruentialGenerator lcg;
    private int capacity;
    private int servers;
    private int[] timeChegada;
    private int[] timeSaida;
    private double tempoGlobal;
    private double tempoGlobalChegada;
    private double tempoGlobalSaida;

    public Simulator(PriorityQueue<Event> chegada, PriorityQueue<Event> saida, PriorityQueue<Event> filaPq,
            int capacity, int servers,
            int[] timeChegada, int[] timeSaida) {
        this.chegada = chegada;
        this.saida = saida;
        this.filaPq = filaPq;
        this.capacity = capacity;
        this.servers = servers;
        this.tempoGlobal = 0;
        this.losses = new ArrayList<Event>();
        this.lcg = new LinearCongruentialGenerator();
        this.timeChegada = timeChegada;
        this.timeSaida = timeSaida;
    }

    public void chegada(Event event) {
        acumulaTempo(event);
        chegada.poll();
        if (chegada.size() + 1 < capacity) {
            if (saida.size() < servers) {
                System.out.println("RETIREI UMA CHEGADA");
                outEvent(event);
            } else {
                inEvent(event);
            }
        } else {
            loss(event);
        }
        inEvent();
    }

    public void saida(Event event) {
        acumulaTempo(event);
        saida.poll();
        System.out.println("RETIREI UMA SAIDA");
        if (chegada.size() > 0) {
            outEvent(chegada.element());
        }
    }

    public Event nextEvent() {
        Event event;
        if (chegada.size() == 0 && tempoGlobal == 0) {
            System.out.println("cria primeiro evento de CHEGADA");
            event = new Event(2, EventType.CHEGADA);
            chegada.add(event);
            return event;
        }
        if (chegada.element().tempo < saida.element().tempo) {
            return chegada.element();
        }
        return saida.element();
    }

    public void inEvent() {
        if (chegada.size() < capacity) {
            System.out.println("cria evento de ENTRADA");
            double tempoEvento = timeChegada[0] + ((timeChegada[1] - timeChegada[0]) * lcg.NextRandom());
            Event eventChegada = new Event(tempoEvento + tempoGlobalChegada, EventType.CHEGADA);
            tempoGlobalChegada = eventChegada.tempo;
            chegada.add(eventChegada);
        }
    }

    public void inEvent(Event eventoChegada) {
        System.out.println("cria evento de ENTRADA REMARCADO");
        Event eventoChegadaRemarcado = new Event(tempoGlobalSaida, EventType.CHEGADA);
        chegada.add(eventoChegadaRemarcado);
    }

    public void outEvent(Event eventChegada) {
        System.out.println("cria evento de SAIDA");
        double tempoEvento = timeSaida[0] + ((timeSaida[1] - timeSaida[0]) * lcg.NextRandom());
        Event eventSaida = new Event(eventChegada.tempo + tempoEvento, EventType.SAIDA);
        tempoGlobalSaida = eventSaida.tempo;
        saida.add(eventSaida);
    }

    public void acumulaTempo(Event event) {
        this.tempoGlobal = event.tempo;
        System.out.println("tempo global:" + tempoGlobal);
    }

    public void loss(Event event) {
        losses.add(event);
        System.out.println("perdeu hahaha");
    }
}
