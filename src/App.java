public class App {
    public static void main(String[] args) {
        int[] chegada = {2,5};
        int[] saida = {3,5};
        Simulator sim = new Simulator(chegada[0],chegada[1],saida[0],saida[1]);
        int count = 100000;
        // Gera 10000 números pseudoaleatórios
        for (int i = 0; i < count; i++) {
            Event event = sim.nextEvent();

            if(event.tipo == EventType.CHEGADA ){
                sim.chegada(event);
            } else if(event.tipo == EventType.SAIDA ){
                sim.saida(event);
            }
        }
        sim.displayResults();
    }
}
