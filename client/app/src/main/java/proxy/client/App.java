package proxy.client;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class App {


  public static void main(String[] args) throws InterruptedException, FileNotFoundException {
    String tableName = args[0];
    try (CqlSession session = session(Arrays.copyOfRange(args, 1, args.length))) {
      printLocal(session);
      printPeers(session);
      createSchema(session, tableName);
      int counter = 0;
      while (true) {
        write(session, counter, tableName);
        Thread.sleep(1000);
        counter++;
      }
    }
  }

  private static void write(CqlSession session, int counter, String tableName) {
    session.execute("insert into test." + tableName + " (id) VALUES (" + counter + ")");
    System.out.println("Written " + counter + " into " + tableName);
  }

  private static void createSchema(CqlSession session, String table) {
    session.execute("CREATE KEYSPACE IF NOT EXISTS test\n"
                    + "  WITH REPLICATION = {"
        + "   'class' : 'SimpleStrategy',"
        + "   'replication_factor' : 2"
        + "  }");
    session.execute("  CREATE TABLE IF NOT EXISTS test." + table + " ("
                    + "  id int PRIMARY KEY,"
        + "  )");
  }

  private static void printLocal(CqlSession session) {
    InetAddress local = session.execute("select listen_address from system.local").one()
        .getInetAddress("listen_address");
    System.out.println("-----------------------------------");
    System.out.println("Local: " + local);
    System.out.println("-----------------------------------");
  }

  private static void printPeers(CqlSession session) {
    ResultSet rs = session.execute(
                    "SELECT peer FROM system.peers");
    System.out.println("-----------------------------------");
    System.out.println("peers:");
    for (Row row : rs.all()) {
      System.out.println(
                        row.getInetAddress("peer"));
    }
    System.out.println("-----------------------------------");
  }


  private static CqlSession session(String[] hosts) throws FileNotFoundException {
    CqlSessionBuilder builder = CqlSession.builder();
    for (String host : hosts) {
      builder.addContactPoint(new InetSocketAddress(host, 9042));
    }
    return builder.withLocalDatacenter("datacenter1").build();
  }
}
