package edu.hm.shareit.mediaService;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Representation of a MediaServiceResult.
 * Containing information: Status and Code
 */
public enum MediaServiceResult {
    OK(Status.OK, response(Status.OK, "Everything ok.")),
    ILLEGAL_ISBN(Status.FORBIDDEN, response(Status.FORBIDDEN, "ISBN is not written in ISBN-13 standard!")),

    ILLEGAL_BARCODE(Status.FORBIDDEN, response(Status.FORBIDDEN, "Barcode is not written in EAN-Barcode-13 standard!")),

    ALREADY_EXISTS(Status.CONFLICT, response(Status.CONFLICT, "Failed adding the Medium to the collection because it already exists.")),

    MISSING_ARG(Status.BAD_REQUEST, response(Status.BAD_REQUEST, "ISBN or Barcode missing!")),

    NOT_FOUND(Status.NOT_FOUND, response(Status.NOT_FOUND, "Medium could not be found!")),

    FORBIDDEN(Status.FORBIDDEN, response(Status.FORBIDDEN, "Null not allowed for operation!")),

    UNMATCHING_ISBN(Status.BAD_REQUEST, response(Status.BAD_REQUEST, "No Book with that ISBN found")),

    UNMATCHING_BARCODE(Status.BAD_REQUEST, response(Status.BAD_REQUEST, "No disc with that Barcode found"));

    private final Response.Status status;
    private final Response.ResponseBuilder response;

    /**
     * Constructor of MediaServiceResult.
     *
     * @param status   The status of the result
     * @param response The response of the result that will be build
     */
    MediaServiceResult(Status status, Response.ResponseBuilder response) {
        this.status = status;
        this.response = response;
    }

    /**
     * Getter for response.
     *
     * @return The response
     */
    public Response getResponse() {
        return this.response.build();
    }

    /**
     * Getter for status.
     *
     * @return The status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Getter for the StatusCode.
     *
     * @return The StatusCode
     */
    public int getCode() {
        return getStatus().getStatusCode();
    }

    /** Creates an response given a Status and detail about the response.
     *
     * @param status The status code
     * @param detail The detail about the response
     * @return A response
     */
    private static Response.ResponseBuilder response(Status status, String detail) {
        final String reason = reason(status.getStatusCode(), detail);
        final Response.ResponseBuilder build = Response.status(status).entity(reason);
        return build;
    }

    /**
     * Given a reason it will build an error detail as JSON object.
     *
     * @param code   The code
     * @param detail The reason why the error occured
     * @return JSON object of the error detail
     */
    public static String reason(int code, String detail) {
        return convertToJson(new ResponseMessage(code, detail));
    }

    /**
     * Converts an object into an JSON-Object representation.
     *
     * @param object The Target
     * @return JSON-Object representation
     */
    private static String convertToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.out.println("MediaServiceResult >>> convertToJson() >> Error");
            return "";
        }
    }

    static class ResponseMessage {
        final String code;
        final String detail;

        public ResponseMessage (){
            this.code = "";
            this.detail = "";
        }

        public ResponseMessage(int code, String message) {
            this.code = "" + code;
            this.detail = message;
        }

        public String getCode() {
            return code;
        }

        public String getDetail() {
            return detail;
        }
    }
}