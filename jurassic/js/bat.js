var Quiz = (function(){
    var $ = {};
    var data = [];
    var categoryIndex = -1;
    var labels = [
        "category",
        "categoryName",
        "categoryDescription",
        "test",
        "testName",
        "testDescription",
        "sampleData",
        "code",
        "results"
    ];
    var aquireComponents = function(){
        var out = {};
        labels.forEach(function(label){
            out[label] = document.getElementById(label);
        });
        return out;
    };
    var populateMenu = function(selector,objArray){
        clearMenu(selector);
        objArray.forEach(function(obj){
            var option = document.createObject("option");
            option.text=obj.name;
            x.add(option,null);
        });
    };
    var clearMenu = function(selector){
        while(0 < selector.options.length){
            selector.remove(0);
        }
    };
    var makeSample = function(name,testcase){
        return name + "(" + testcase.params + ") --> " + testcase.result;
    }
    var runTests = function(name,testcases,code){
        eval(code);
        var out = {};
        testcases.forEach(function(testCase){
            var actual = window[name].apply(undefined, testCase.params);
            out[makeSample(name,testCase)] = (actual == testCase.result);
        });
        return out;
    };
    return {
        launch:function(tests){
            $ = aquireComponents();
            populateMenu($.category,tests);
            data = tests;
        },
        pickCategory:function(){
            //TODO - clear previous test
            categoryIndex = $.category.selectedIndex;
            if (-1 < categoryIndex) {
                $.categoryDescription.innerHTML = data[categoryIndex].description;
                populateMenu($.test,data[categoryIndex].tests);
            }
        },
        pickTest:function(){
            var index = $.test.selectedIndex;
            if (-1 < index) {
                $.testDescription.innerHTML = data[index].description;

            }
        },
        runTests:function(){
        }
    };
})();