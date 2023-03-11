package proxy.client;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

public class App {


  public static void main(String[] args) throws InterruptedException, FileNotFoundException {

    while (true) {
      try (CqlSession session = session(args)) {

        ResultSet rs = session.execute(
            "select release_version from system.local");
        Row row = rs.one();
        System.out.println(
            row.getString("release_version"));

      }
      Thread.sleep(1000);
    }

  }

  private static CqlSession session(String[] hosts) throws FileNotFoundException {
    CqlSessionBuilder builder = CqlSession.builder();
    for (String host : hosts) {
      builder.addContactPoint(new InetSocketAddress(host, 9042));
    }
    return builder.withLocalDatacenter("datacenter1").build();
  }



}
