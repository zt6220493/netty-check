//function Queue(){  
//    //存储元素数组  
//    var aElement = new Array();  
//    /* 
//    * @brief: 元素入队 
//    * @param: vElement元素列表 
//    * @return: 返回当前队列元素个数 
//    * @remark: 1.EnQueue方法参数可以多个 
//    *    2.参数为空时返回-1 
//    */  
//    Queue.prototype.EnQueue = function(vElement){  
//        if (arguments.length == 0)  
//            return - 1;  
//        //元素入队  
//        for (var i = 0; i < arguments.length; i++){  
//            aElement.push(arguments[i]);  
//        }  
//        return aElement.length;  
//    }  
//    /* 
//    * @brief: 元素出队 
//    * @return: vElement 
//    * @remark: 当队列元素为空时,返回null 
//    */  
//    Queue.prototype.DeQueue = function(){  
//        if (aElement.length == 0)  
//            return null;  
//        else  
//            return aElement.shift();  
//   
//    }  
//    /* 
//    * @brief: 获取队列元素个数 
//    * @return: 元素个数 
//    */  
//    Queue.prototype.GetSize = function(){  
//        return aElement.length;  
//    }  
//    /* 
//    * @brief: 返回队头素值 
//    * @return: vElement 
//    * @remark: 若队列为空则返回null 
//    */  
//    Queue.prototype.GetHead = function(){  
//        if (aElement.length == 0)  
//            return null;  
//        else  
//            return aElement[0];  
//    }  
//    /* 
//    * @brief: 返回队尾素值 
//    * @return: vElement 
//    * @remark: 若队列为空则返回null 
//    */  
//    Queue.prototype.GetEnd = function(){  
//        if (aElement.length == 0)  
//            return null;  
//        else  
//            return aElement[aElement.length - 1];  
//    }  
//    /* 
//    * @brief: 将队列置空 
//    */  
//    Queue.prototype.MakeEmpty = function(){  
//        aElement.length = 0;  
//    }  
//    /* 
//    * @brief: 判断队列是否为空 
//    * @return: 队列为空返回true,否则返回false 
//    */  
//    Queue.prototype.IsEmpty = function(){  
//        if (aElement.length == 0)  
//            return true;  
//        else  
//            return false;  
//    }  
//    /* 
//    * @brief: 将队列元素转化为字符串 
//    * @return: 队列元素字符串 
//    */  
//    Queue.prototype.toString = function(){  
//        var sResult = (aElement.reverse()).toString();  
//        aElement.reverse()  
//        return sResult;  
//    }  
//} 
/*
 * MAP对象，实现MAP功能
 *
 * 接口：
 * size()     获取MAP元素个数
 * isEmpty()    判断MAP是否为空
 * clear()     删除MAP所有元素
 * put(key, value)   向MAP中增加元素（key, value) 
 * remove(key)    删除指定KEY的元素，成功返回True，失败返回False
 * get(key)    获取指定KEY的元素值VALUE，失败返回NULL
 * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
 * containsKey(key)  判断MAP中是否含有指定KEY的元素
 * containsValue(value) 判断MAP中是否含有指定VALUE的元素
 * values()    获取MAP中所有VALUE的数组（ARRAY）
 * keys()     获取MAP中所有KEY的数组（ARRAY）
 *
 * 例子：
 * var map = new Map();
 *
 * map.put("key", "value");
 * var val = map.get("key")
 * ……
 *
 */
function Map() {
    this.elements = new Array();
 
    //获取MAP元素个数
    this.size = function() {
        return this.elements.length;
    }
 
    //判断MAP是否为空
    this.isEmpty = function() {
        return (this.elements.length < 1);
    }
 
    //删除MAP所有元素
    this.clear = function() {
        this.elements = new Array();
    }
 
    //向MAP中增加元素（key, value) 
    this.put = function(_key, _value) {
        this.elements.push( {
            key : _key,
            value : _value
        });
    }
 
    //删除指定KEY的元素，成功返回True，失败返回False
    this.remove = function(_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    }
 
    //获取指定KEY的元素值VALUE，失败返回NULL
    this.get = function(_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return null;
        }
    }
 
    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
    this.element = function(_index) {
        if (_index < 0 || _index >= this.elements.length) {
            return null;
        }
        return this.elements[_index];
    }
 
    //判断MAP中是否含有指定KEY的元素
    this.containsKey = function(_key) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    }
 
    //判断MAP中是否含有指定VALUE的元素
    this.containsValue = function(_value) {
        var bln = false;
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].value == _value) {
                    bln = true;
                }
            }
        } catch (e) {
            bln = false;
        }
        return bln;
    }
 
    //获取MAP中所有VALUE的数组（ARRAY）
    this.values = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    }
 
    //获取MAP中所有KEY的数组（ARRAY）
    this.keys = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    }
}
var pageQuene = new Map();