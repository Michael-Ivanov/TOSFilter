import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class APIService {
    List<Symbol> symbolList;
    List<Integer> checkList;
    private boolean isRunning = true;


    public APIService(List<Symbol> symbolList, List<Integer> checkList) {
        this.symbolList = symbolList;
        this.checkList = checkList;
    }

    public String command(String apiCommand) throws IOException {
        StringBuilder response = new StringBuilder();
        String str;
        HttpURLConnection regLvl1Connection = null;
        URL url = new URL(apiCommand);
        regLvl1Connection = (HttpURLConnection) url.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(regLvl1Connection.getInputStream()))) {

            while ((str = in.readLine()) != null) {
                response.append(str);
            }
            return response.toString();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return "Error. Something went wrong.";
    }

    public void regSymbolTOSStream(int port) {

        String strUrl = "";
        HttpURLConnection connection = null;
        for (Symbol symbol : symbolList) {
            try {
                strUrl = "http://localhost:8080/SetOutput?symbol=" + symbol.getName()
                        + "&feedtype=TOS&output=" + port + "&status=on";
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                final StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println(content.toString());
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void readerTOSStream(int port) {
        Runnable runnable = () -> {
            try {
                DatagramSocket socket = new DatagramSocket(port);
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                StringBuilder str = new StringBuilder();
                while (isRunning) {
                    socket.receive(packet);
                    byte[] packetArr = packet.getData();
                    for (byte b : packetArr) {
                        str.append((char) b);
                    }

//                    для отладки
//                    System.out.println(str.toString().trim());

                    String name = str.substring(str.indexOf("Symbol") + 7, str.indexOf("Type") - 1);
                    double price = Double.parseDouble(
                            str.substring(str.indexOf("Price") + 6, str.indexOf("Size") - 1));
                    int size = Integer.parseInt(
                            str.substring(str.indexOf("Size") + 5, str.indexOf("Source") - 1));
                    String time = str.substring(str.indexOf("LocalTime") + 10, str.indexOf("Message") - 5);

//                    System.out.println("name: " + name + " price: " + price + " size: " + size + " time: " + time);

                    for (Symbol symbol : symbolList) {
                        if (symbol.getName().equals(name)) {
                            if (!symbol.getTime().equals(time)) {
                                symbol.clearParts();
                            }
                            symbol.addPart(size);
                            symbol.setPrice(price);
//                            symbol.setSize(size);
                            symbol.setTime(time);
                            checkSizeConditions(symbol);
                        }
                    }

                    str.setLength(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void checkSizeConditions(Symbol symbol) {
        for (Integer size : checkList) {
            int partSum = 0;
            for (int part : symbol.getParts()) {
                partSum = partSum + part;
                if (partSum == size) {
                    System.out.println(symbol.getName() + " Success! We've found size: " + size + ". Time: " + symbol.getTime());
                    System.out.println(symbol.getName() + ": " +  symbol.getParts());
                    symbol.clearParts();
                    return;
                }
            }
        }
    }
}
