package net.juligames.goodproxy.web;

public record WebResponse<T> (long requestID, long timestamp, T data) {
}
