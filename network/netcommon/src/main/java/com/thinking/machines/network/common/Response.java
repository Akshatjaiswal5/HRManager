package com.thinking.machines.network.common;

public class Response {
 private boolean success;
 private Object result;
 private Object exception;

 public boolean isSuccess() {
  return this.success;
 }

 public void setSuccess(boolean success) {
  this.success = success;
 }

 public Object getResult() {
  return this.result;
 }

 public void setResult(Object result) {
  this.result = result;
 }

 public Object getException() {
  return this.exception;
 }

 public void setException(Object exception) {
  this.exception = exception;
 }

 public boolean hasException() {
  return this.success==false;
 }

}
