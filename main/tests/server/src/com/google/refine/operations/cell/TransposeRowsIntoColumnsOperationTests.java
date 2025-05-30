/*

Copyright 2010, Google Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,           
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY           
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.google.refine.operations.cell;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.node.TextNode;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.refine.RefineTest;
import com.google.refine.model.Project;
import com.google.refine.operations.OperationDescription;
import com.google.refine.operations.OperationRegistry;
import com.google.refine.util.ParsingUtilities;
import com.google.refine.util.TestUtils;

public class TransposeRowsIntoColumnsOperationTests extends RefineTest {

    Project project;

    @Override
    @BeforeTest
    public void init() {
        logger = LoggerFactory.getLogger(this.getClass());
        OperationRegistry.registerOperation(getCoreModule(), "transpose-rows-into-columns", TransposeRowsIntoColumnsOperation.class);
    }

    @BeforeMethod
    public void setUpGrid() {
        project = createProject(
                new String[] { "a", "b", "c" },
                new Serializable[][] {
                        { "1", "2", "3" },
                        { "4", "5", "6" },
                        { "7", "8", "9" },
                        { "10", "11", "12" }
                });
    }

    @Test
    public void testTransposeRowsIntoColumnsOperation() throws Exception {
        String json = "{\"op\":\"core/transpose-rows-into-columns\","
                + "\"description\":"
                + new TextNode(OperationDescription.cell_transpose_rows_into_columns_brief(3, "start column")).toString() + ","
                + "\"columnName\":\"start column\","
                + "\"rowCount\":3}";
        TestUtils.isSerializedTo(ParsingUtilities.mapper.readValue(json, TransposeRowsIntoColumnsOperation.class), json);
    }

    @Test
    public void testValidate() {
        assertThrows(IllegalArgumentException.class, () -> new TransposeRowsIntoColumnsOperation(null, 2).validate());
    }

    @Test
    public void testColumnsDiff() {
        assertEquals(new TransposeRowsIntoColumnsOperation("b", 2).getColumnsDiff(), Optional.empty());
    }

    @Test
    public void testColumnsDependencies() {
        assertEquals(new TransposeRowsIntoColumnsOperation("b", 2).getColumnDependencies(), Optional.of(Set.of("b")));
    }

    @Test
    public void testRename() {
        var SUT = new TransposeRowsIntoColumnsOperation("b", 2);

        TransposeRowsIntoColumnsOperation renamed = SUT.renameColumns(Map.of("b", "c"));

        TestUtils.isSerializedTo(renamed, "{\n"
                + "  \"columnName\" : \"c\",\n"
                + "  \"description\" : " + new TextNode(OperationDescription.cell_transpose_rows_into_columns_brief(2, "c")).toString()
                + ",\n"
                + "  \"op\" : \"core/transpose-rows-into-columns\",\n"
                + "  \"rowCount\" : 2\n"
                + "}");
    }

    @Test
    public void testTransposeRowsIntoColumns() throws Exception {
        TransposeRowsIntoColumnsOperation operation = new TransposeRowsIntoColumnsOperation("b", 2);

        runOperation(operation, project);

        Project expected = createProject(
                new String[] { "a", "b 1", "b 2", "c" },
                new Serializable[][] {
                        { "1", "2", "5", "3" },
                        { "4", null, null, "6" },
                        { "7", "8", "11", "9" },
                        { "10", null, null, "12" }
                });

        assertProjectEquals(project, expected);
    }

    @Test
    public void testTransposeRecordsIntoRows() throws Exception {
        Project withRecords = createProject(
                new String[] { "a", "b", "c" },
                new Serializable[][] {
                        { "1", "2", "3" },
                        { null, "5", null },
                        { "7", "8", "9" },
                        { null, "11", null }
                });

        TransposeRowsIntoColumnsOperation operation = new TransposeRowsIntoColumnsOperation("b", 2);

        runOperation(operation, withRecords);

        Project expected = createProject(
                new String[] { "a", "b 1", "b 2", "c" },
                new Serializable[][] {
                        { "1", "2", "5", "3" },
                        { "7", "8", "11", "9" }
                });

        assertProjectEquals(withRecords, expected);
    }

}
