package org.bahmni.csv;

import junit.framework.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class RowResultTest {
    @Test
    public void isSuccessful_returns_true_when_no_errormessage() {
        RowResult successfulRow = new RowResult(new DummyCSVEntity("1", "name"));
        Assert.assertTrue("isSuccessful() should be true, as there is no Error Message", successfulRow.isSuccessful());
        Assert.assertTrue("isSuccessful() should be true, as there is no Error Message", new RowResult(new DummyCSVEntity()).isSuccessful());
    }

    @Test
    public void isSuccessful_returns_false_for_empty_errormessage() {
        RowResult rowResult = new RowResult(new DummyCSVEntity("1", "name"), "");
        Assert.assertFalse("isSuccessful() should be true, as there is no Error Message", rowResult.isSuccessful());
    }

    @Test
    public void isSuccessful_returns_false_when_no_errormessage() {
        RowResult validationFailedRow = new RowResult(new DummyCSVEntity("1", "name"), new FileNotFoundException("file not found"));
        Assert.assertFalse("isSuccessful() should be false, as there is an Error Message", validationFailedRow.isSuccessful());
        Assert.assertTrue("Row Error should start with the row details. " + validationFailedRow.getRowWithErrorColumnAsString(), validationFailedRow.getRowWithErrorColumnAsString().startsWith("1,name"));
        Assert.assertTrue("Row Error should contain the exception stack trace. " + validationFailedRow.getRowWithErrorColumnAsString(), validationFailedRow.getRowWithErrorColumnAsString().contains("file not found"));
    }

    @Test
    public void getRowWithErrorColumn_returns_error_message_for_exceptions() {
        RowResult validationFailedRow = new RowResult(new DummyCSVEntity("1", "name"), new FileNotFoundException("file not found"));
        String[] rowWithErrorColumn = validationFailedRow.getRowWithErrorColumn();
        Assert.assertTrue("validation error message has stacktrace",
                rowWithErrorColumn[rowWithErrorColumn.length - 1].startsWith("file not found"));
    }

    @Test
    public void getRowWithErrorColumn_returns_error_message_for_string_messages() {
        RowResult validationFailedRow = new RowResult(new DummyCSVEntity("1", "name"), "validation error");
        String[] rowWithErrorColumn = validationFailedRow.getRowWithErrorColumn();
        Assert.assertEquals("validation error", rowWithErrorColumn[rowWithErrorColumn.length - 1]);
    }

    @Test
    public void getRowWithErrorColumn_returns_inner_error_message() {
        RowResult validationFailedRow = new RowResult(new DummyCSVEntity("1", "name"), new Exception(new Exception(new FileNotFoundException("file not found"))));
        String[] rowWithErrorColumn = validationFailedRow.getRowWithErrorColumn();
        Assert.assertTrue("validation error message should have inner exception message",
                rowWithErrorColumn[rowWithErrorColumn.length - 1].contains("file not found"));
    }

}
