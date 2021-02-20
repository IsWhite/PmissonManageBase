$(function () {
    //初始化加载数据
    $("#jqGrid").jqGrid({
        url: baseURL + 'proc/procMold/list',
        datatype: "json",
        //数据字段一一绑定 <> 根据name
        colModel: [
            {label: 'Id', name: 'id', index: "id", width: 45, key: true ,hidden:true},
            {label: '类型编码', name: 'moldCode', index: "mold_code", width: 75},
            {label: '商品类别', name: 'categoryName', index: "category_name", width: 75},
            {label: '商品类型', name: 'moldName', index: "mold_name", width: 75},
            {label: '创建人', name: 'createrName', index: "createrName", width: 75},
            {label: '创建时间', name: 'createTime', index: "create_time", width: 85},
            {label: '修改时间', name: 'reviserTime', index: "reviser_time", width: 75},
            {label: '排序', name: 'orderNum', index: "order_num", width: 75},
            {
                label: '是否启用', name: 'isqy', index: "isqy", width: 75, formatter: function (value, options, row) {
                    return value === 0 ?
                        '<span class="label label-danger">禁用</span>' :
                        '<span class="label label-success">启用</span>'
                }
            },
        ],
        viewrecords: true,
        height: 425,
        rowNum: 10,
        rowList: [10, 20, 40, 60, 80, 100, 120, 200],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        //读取服务器返回的json数据并解析
        jsonReader: {
            root: "data.page.list",
            page: "data.page.currPage",
            total: "data.page.totalPage",
            records: "data.page.totalCount"
        },
        //设置jqGrid将要向服务端传递的参数名称
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var vm = new Vue({
    el: '#pmpapp',
    data: {
        q: {
            moldName: null
        },
        showList: true,
        title: null,
        moldList: {},
        mold: {
            id: null,
            moldName: null,
            moldCode: null,
            moldImg: null,
            orderNum: 0,
            isqy: 1,
            categoryId:''

        },
        selected: '-1',
        dataList: []
    },

    created(){
        this.getCategory();
    },
    //请求方法
    methods: {

        //查询
        query: function () {
            vm.reload();
        },

        //重新load数据时的请求方法
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'search': vm.q.moldName},  //postData是post请求参数Data
                page: page
            }).trigger("reloadGrid");
        },

        //重置
        reset: function () {
            window.location.href = baseURL + "modules/proc/procMold.html";
        },

        //进入新增
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.moldList = {};
            vm.mold = {
                id: null,
                moldName: null,
                moldCode: null,
                moldImg: null,
                isqy: 1,
            }
            vm.selected = '-1';
        },
        //获得商品类别
        getCategory(){
            $.get(baseURL + "proc/procMold/getCategory/",function (r){
                vm.dataList = r.data;
                //vm.selected = r.data[0].value;  //默认选第一个
            })
        },

        //进入修改
        update: function () {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id);
        },

        //获取详情
        getInfo: function (id) {
            $.get(baseURL + "proc/procMold/info/" + id, function (r) {
                vm.mold = r.data.mold;
                vm.selected = r.data.mold.categoryId;
            });
        },
        //删除
        del: function () {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                var data = JSON.stringify(ids);
                console.log(data);
                $.ajax({
                    type: "POST",
                    url: baseURL + "proc/procMold/delete",
                    contentType: "application/json",
                    data: data,
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function () {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },

        //保存-更新数据
        saveOrUpdate: function () {
            var url = vm.mold.id == null ? "proc/procMold/save" : "proc/procMold/update";
            vm.mold.categoryId = (vm.selected == -1? '':vm.selected);
            console.log(vm.mold)

            //isNumber校验是否为数字， common.js中
            if(!isNumber(vm.mold.orderNum)){
                layer.alert("排序应为数字！");
                return false;
            }
                $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.mold),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        }

    }
});