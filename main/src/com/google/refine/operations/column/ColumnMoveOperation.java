/*

Copyright 2010,2012. Google Inc.
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

package com.google.refine.operations.column;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.Validate;

import com.google.refine.model.Project;
import com.google.refine.operations.OperationDescription;

public class ColumnMoveOperation extends AbstractColumnMoveOperation {

    final protected int _index;

    @JsonCreator
    public ColumnMoveOperation(
            @JsonProperty("columnName") String columnName,
            @JsonProperty("index") int index) {
        super(columnName);
        _index = index;
    }

    @Override
    public void validate() {
        super.validate();
        Validate.isTrue(_index >= 0, "Invalid column index");
    }

    @JsonProperty("index")
    public int getIndex() {
        return _index;
    }

    @Override
    protected int getNewColumnIndex(int currentIndex, Project project) {
        if (_index < 0 || _index >= project.columnModel.columns.size()) {
            throw new IllegalArgumentException("New column index out of range " + _index);
        }
        return _index;
    }

    @Override
    protected String getBriefDescription(Project project) {
        return OperationDescription.column_move_brief(_columnName, _index);
    }

    @Override
    public ColumnMoveOperation renameColumns(Map<String, String> newColumnNames) {
        return new ColumnMoveOperation(newColumnNames.getOrDefault(_columnName, _columnName), _index);
    }
}
