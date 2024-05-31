package ru.megaeater42.proxy;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "nasa-api-client-service",  url = "https://api.nasa.gov/planetary")
public interface NasaApiProxy extends NasaApiApodProxy {
}
