public class Event implements Comparable<Event> {
    double tempo;
    EventType tipo;

    public Event(double tempo, EventType tipo) {
        this.tempo = tempo;
        this.tipo = tipo;
    }

    @Override
    public int compareTo(Event outroEvento) {
        return Double.compare(this.tempo, outroEvento.tempo);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "tempo=" + tempo +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}