package righiefini.utente.righi.arduino;

import java.io.Serializable;

/**
 * Created by Utente on 13/02/2018.
 */

public class Value implements Serializable {
    String acqua;
    String umiditàTerreno;
    String umiditàAria;
    String temperatura;
    String staoPompa;
    String statoVentola;
    String automatica;

    public void setAcqua(String acqua) {
        this.acqua = acqua;
    }

    public void setUmiditàTerreno(String umiditàTerreno) {
        this.umiditàTerreno = umiditàTerreno;
    }

    public void setUmiditàAria(String umiditàAria) {
        this.umiditàAria = umiditàAria;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public void setStaoPompa(String staoPompa) {
        this.staoPompa = staoPompa;
    }

    public void setStatoVentola(String statoVentola) {
        this.statoVentola = statoVentola;
    }

    public String getAcqua() {
        return acqua;
    }

    public String getUmiditàTerreno() {
        return umiditàTerreno;
    }

    public String getUmiditàAria() {
        return umiditàAria;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public String getStaoPompa() {
        return staoPompa;
    }

    public String getStatoVentola() {
        return statoVentola;
    }

    public String getAutomatica() {
        return automatica;
    }


    public void setAutomatica(String automatica) {
        this.automatica = automatica;
    }


}
