import java.util.PriorityQueue;

public class Simulator {
    private double tempoChegadaMin;
    private double tempoChegadaMax;
    private double tempoServicoMin;
    private double tempoServicoMax;
    private double firstArrivalTime = 2;

    // Objects
    private PriorityQueue<Event> scheduler;
    private RandomGeneratorForQueue randomGenerator;

    // Runtime Parameters
    private double globalTime = 0.0;
    
    private double[] queueTimes;

    public Simulator(double tempoChegadaMin, double tempoChegadaMax, double tempoServicoMin, double tempoServicoMax) {
        this.tempoChegadaMin = tempoChegadaMin;
        this.tempoChegadaMax = tempoChegadaMax;
        this.tempoServicoMin = tempoServicoMin;
        this.tempoServicoMax = tempoServicoMax;

        Queue.reset();
        
        scheduler = new PriorityQueue<>();
        randomGenerator = new RandomGeneratorForQueue();
        queueTimes = new double[Queue.capacity() + 1];

        scheduler.add(new Event(EventType.CHEGADA, firstArrivalTime));
    }

    public Event nextEvent(){
        return scheduler.poll();
    }

    private double randomTimeBetween(double minTime, double maxTime) {
        return (maxTime - minTime) * randomGenerator.NextRandom() + minTime;
    }
    
    public void acumulaTempo(double eventTime){
        double delta = eventTime - globalTime;
        globalTime = eventTime;

        queueTimes[Queue.status()] += delta;
    }

    public void chegada(Event event){
        acumulaTempo(event.tempo);

        if (Queue.status() < Queue.capacity()) {
            Queue.in();

            if (Queue.status() <= Queue.servers()) {
                scheduler.add(
                        new Event(EventType.SAIDA,
                                globalTime + randomTimeBetween(tempoServicoMin, tempoServicoMax)));
            }
        } else {
            Queue.loss();
        }

        scheduler.add(new Event(EventType.CHEGADA,
                globalTime + randomTimeBetween(tempoChegadaMin, tempoChegadaMax)));
    }
    
    public void saida(Event event){
        acumulaTempo(event.tempo);

        Queue.out();
        if (Queue.status() >= Queue.servers()) {
            scheduler.add(new Event(EventType.SAIDA,
                    globalTime + randomTimeBetween(tempoServicoMin, tempoServicoMax)));
        }
    }

    public void displayResults() {
        System.out.printf("Tempo de simulação total: %.2f u.t\n\n", globalTime);

        System.out.println("Tempo em cada estado da fila:");
        for (int i = 0; i < queueTimes.length; i++) {
            System.out.format("\tState %d: %.2f u.t\n", i, queueTimes[i]);
        }
        System.out.println("");

        System.out.println("Probabilidade de cada estado da fila");
        for (int i = 0; i < queueTimes.length; i++) {
            System.out.format("\tProbabilidade do estado %d: %.3f%s \n", i, queueTimes[i] / globalTime * 100, "%");
        }
        System.out.println("");

        System.out.println("Clientes perdidos: " + Queue.getLossCounter());
    }
}
