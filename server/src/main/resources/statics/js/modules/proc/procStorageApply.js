$(function () {
    //初始化加载数据
    $("#jqGrid").jqGrid({
        url: baseURL + 'proc/procStorage/applyList',
        datatype: "json",
        //数据字段一一绑定 <> 根据name
        colModel: [
            {label: 'Id', name: 'id', index: "id", width: 45, key: true, hidden: true},
            {label: '仓库编码', name: 'storageCode', index: "storage_code", width: 75},
            {label: '仓库名称', name: 'storageName', index: "storage_name", width: 75},
            {label: '仓库面积', name: 'storageArea', index: "storage_area", width: 75},
            {label: '租期', name: 'rentTime', index: "rent_time", width: 75},
            {label: '起租日期', name: 'staRentDate', index: "sta_rent_date", width: 75},
            {label: '到租日期', name: 'endRentDate', index: "end_rent_date", width: 75},
            {label: '租金', name: 'rentPrice', index: "rent_price", width: 75},
            {label: '租户姓名', name: 'renterName', index: "renter_name", width: 75},
            {label: '租户电话', name: 'renterPhone', index: "renter_phone", width: 75},
            {label: '房东姓名', name: 'landlordName', index: "landlord_name", width: 75},
            {label: '房东电话', name: 'landlordPhone', index: "landlord_phone", width: 75},
            {label: '仓库门牌号', name: 'storageDoorNum', index: "storage_doorNum", width: 75},
            {
                label: '是否到期', name: 'isExpire', index: "is_expire", width: 75, formatter: function (value) {
                    return value === 0?
                        '<span>是</span>' :
                        '<span>否</span>'
                }
            },
            {label: '创建人', name: 'createrName', index: "creater_name", width: 75},
            {label: '审核人', name: 'approverName', index: "approver_name", width: 75},
            {label: '创建时间', name: 'createTime', index: "create_time", width: 85},
            {
                label: '申请状态', name: 'itsmStatus', index: "itsm_status", width: 75, formatter: function (value) {
                    if(value === -1){
                        return '<span>草稿</span>' ;
                    }else if (value === 1){
                        return '<span>流转中</span>' ;
                    }else if (value === 2){
                        return '<span>办结</span>' ;
                    }else if (value === 3){
                        return '<span>作废</span>' ;
                    }

                }
            },
            // {
            //     label: '是否启用', name: 'isqy', index: "isqy", width: 75, formatter: function (value, options, row) {
            //         return value === 0 ?
            //             '<span class="label label-danger">禁用</span>' :
            //             '<span class="label label-success">启用</span>'
            //     }
            // },
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
            categoryId: ''

        },
        selected: '-1',
        dataList: []
    },

    created() {
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
        getCategory() {
            $.get(baseURL + "proc/procMold/getCategory/", function (r) {
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
            vm.mold.categoryId = (vm.selected == -1 ? '' : vm.selected);
            console.log(vm.mold)

            //isNumber校验是否为数字， common.js中
            if (!isNumber(vm.mold.orderNum)) {
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