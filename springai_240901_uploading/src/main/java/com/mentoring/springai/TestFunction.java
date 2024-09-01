package com.mentoring.springai;

import java.util.function.Function;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component(value = "testFunction")
@Description("test function for mysterious calculation!")
@Slf4j
public class TestFunction implements Function<TestFunction.Request, TestFunction.Response>
{
  public static class Request
  {
    public double a;
    public double b;

    // 기본 생성자
    public Request() {}

    public Request(double a, double b)
    {
      log.info("call Request()");
      this.a = a;
      this.b = b;
    }
  }

  public static class Response
  {
    public double answer;

    public Response() {};

    public Response(double answer)
    {
      log.info("call Response()");
      this.answer = answer;
    }
  }

  @Override
  public Response apply(Request t)
  {
    log.info("call apply()");
    return new Response(t.a * t.b * 2  - 16);
  }
}

