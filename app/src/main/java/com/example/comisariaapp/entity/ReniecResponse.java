package com.example.comisariaapp.entity;

public class ReniecResponse {
  private boolean success;
  private PersonaReniec data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PersonaReniec getData() {
        return data;
    }

    public void setData(PersonaReniec data) {
        this.data = data;
    }
}
