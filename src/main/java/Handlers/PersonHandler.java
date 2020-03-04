package Handlers;

import DAOs.Connect;
import DAOs.DataAccessException;
import Service.PersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonHandler extends FileHandler {
    private PersonService pService = new PersonService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            if (httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
                Headers reqHeaders = httpExchange.getRequestHeaders();
                InputStream reqBody = httpExchange.getRequestBody();
                String reqData = readString(reqBody);
                System.out.println(reqData);
                String uri = httpExchange.getRequestURI().toString();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    /* Not going to check the auth token here - need to rewrite all lower classes
                    if (foundToken(authToken)) {
                        String response;
                        String personID = getPersonID(uri);
                        if(!personID.equals("")) { //TODO: Add a reader to check if there is a given personID
                            response = Deserialize.serialize(pService.getPerson(authToken, personID));
                        } else {
                            response = Deserialize.serialize(pService.getFamily(authToken));
                        }
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = httpExchange.getResponseBody();
                        writeString(response, respBody);
                        respBody.close();
                    }else {
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 200);
                    } */
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    private String getPersonID(String uri) {
        String personID = "";
        if(uri.length() > 9) {
            personID = uri.substring(9, uri.length() - 1);
        }
        return personID;
    }

    private boolean foundToken(String authToken) {
        boolean isFound = false;
        Connect db = new Connect();

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        String sql = "SELECT * FROM authToken WHERE authToken = ?";
        //get the user associated with the authToken
        ResultSet rs;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if(rs.next()) {
                isFound = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return isFound;
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
