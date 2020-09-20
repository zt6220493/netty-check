var IsAsc = true;
function SortTable(TableID, Col, DataType) {

	// 获取col列 th, 判断是逆序还是顺序
	var sortTh = $("#" + TableID + " th").eq(Col);
	var removeClazz = null;
	var addClazz = null;
	if (sortTh.attr("class").indexOf("sorting") < 0) {
		return;
	}
	if (sortTh.attr("class").indexOf("sorting_desc") >= 0) {
		removeClazz = "sorting_desc";
		addClazz = "sorting_asc";
	} else if (sortTh.attr("class").indexOf("sorting_asc") >= 0) {
		removeClazz = "sorting_asc";
		addClazz = "sorting_desc";
	} else {
		removeClazz = "sorting";
		addClazz = "sorting_asc";
	}
	sortTh.removeClass(removeClazz);
	sortTh.addClass(addClazz);

	IsAsc = !IsAsc;
	var DTable = document.getElementById(TableID);
	var DBody = DTable.tBodies[0];
	var DataRows = DBody.rows;
	var MyArr = new Array;
	for ( var i = 0; i < DataRows.length; i++) {
		MyArr[i] = DataRows[i];
	}
	// 判断上次排序的列和这次是否为同一列
	if (DBody.CurrentCol == Col) {
		MyArr.reverse(); // 将数组倒置
	} else {
		
		clearClass($("#" + TableID + " th").eq(DBody.CurrentCol))
		MyArr.sort(CustomCompare(Col, DataType));
	}
	// 创建一个文档碎片，将所有的行都添加进去，相当于一个暂存架，目的是（如果直接加到document.body里面，会插入一行，就刷新一次，如果数据多了就会影响用户体验）
	// 先将行全部放在暂存架里面，然后将暂存架里面的行 一起添加到document.body，这样表格只会刷新一次。
	// 就像你去商店购物，要先将要买的物品（行）全部写在单子上（文档碎片），然后超市全部购买，而不会想到一样东西就去一次，那么
	var frag = document.createDocumentFragment();
	for ( var i = 0; i < MyArr.length; i++) {
		frag.appendChild(MyArr[i]); // 将数组里的行全部添加到文档碎片中
	}
	DBody.appendChild(frag);// 将文档碎片中的行全部添加到 body中
	DBody.CurrentCol = Col; // 记录下当前排序的列
}

function clearCache(TableID)
{
	var DTable = document.getElementById(TableID);
	var DBody = DTable.tBodies[0];
	clearClass($("#" + TableID + " th").eq(DBody.CurrentCol))
	DBody.CurrentCol = null;

}

function CustomCompare(Col, DataType) {
	return function CompareTRs(TR1, TR2) {
		var value1, value2;
		// 判断是不是有customvalue这个属性
		if (TR1.cells[Col].getAttribute("customvalue")) {
			value1 = convert(TR1.cells[Col].getAttribute("customvalue"),
					DataType);
			value2 = convert(TR2.cells[Col].getAttribute("customvalue"),
					DataType);
		} else {
			// TR2.cells[Col].firstChild.nodeValue
			value1 = convert($(TR1.cells[Col]).html(), DataType);
			value2 = convert($(TR2.cells[Col]).html(), DataType);
		}
		if (value1 < value2)
			return -1;
		else if (value1 > value2)
			return 1;
		else
			return 0;
	};
}
function convert(DataValue, DataType) {

	
	switch (DataType) {
	case "int":
		if (null == DataValue ||　DataValue==''){
			return -9999999;
		}
		return parseInt(DataValue);
	case "float":
		return parseFloat(DataValue);
	case "date":
		if (null == DataValue ||　DataValue==''){
			return 0;
		}
		return new Date(Date.parse(DataValue)).getTime();
	default:
		return DataValue.toString();
	}
}
// typeArr 如果为空，默认为 string
function sortInit(TableID, typeArr) {
	var thList = $("#" + TableID + " th");
	
	typeArr = changeTypeArr(typeArr);
	for ( var int = 0; int < thList.length; int++) {

		if ($(thList[int]).attr("class").indexOf("sorting") < 0) {
			
			continue;
		}
		if (typeArr[int] == null || typeArr[int] == ''){
			
			$(thList[int]).attr("onclick","SortTable('"+TableID+"',"+int+")")
		}
		else{
			$(thList[int]).attr("onclick","SortTable('"+TableID+"',"+int+",'"+typeArr[int]+"')")
		}
	}
}

function changeTypeArr(typeArr){
//	var typeArr = ['0_int','3_date'];
	var dateType = [];
	if (typeArr == null){
		return dateType;
	}
	for ( var int = 0; int < typeArr.length; int++) 
	{
		if (typeArr[int].indexOf("_")>=0 )
		{
			var a = typeArr[int].split("_");
			dateType[a[0]] = a[1];
		}
	}
	return dateType;
}

function clearClass(obj){
	var sortTh = obj;
	if (sortTh.length == 0){
		return ;
	}
	var clazz = sortTh.attr("class");
	
	// 不管class 是否包含 “sorting_asc”或者“sorting_desc” ，都替换成“sorting”,
	clazz = clazz.replace("sorting_asc","sorting");
	clazz = clazz.replace("sorting_desc","sorting");
	
	sortTh.attr("class",clazz);
}