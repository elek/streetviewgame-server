package net.messze.valahol.data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Response for a guess.
 */
@XmlRootElement
public class SolutionResponse {
    /**
     * True if the solution was good.
     */
    private boolean good;

    /**
     * Textual response in the case of a wrong response.
     */
    private String response;

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
