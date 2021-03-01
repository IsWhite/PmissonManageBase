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
            {label: '仓库门牌号', name: 'storageDoornum', index: "storage_doornum", width: 75},
            {
                label: '是否到期', name: 'isExpire', index: "is_expire", width: 75, formatter: function (value) {
                    return value === 0 ?
                        '<span>是</span>' :
                        '<span>否</span>'
                }
            },
            {label: '创建人', name: 'createrName', index: "creater_name", width: 75},
            {label: '审核人', name: 'approverName', index: "approver_name", width: 75},
            {label: '创建时间', name: 'createTime', index: "create_time", width: 85},
            {
                label: '申请状态', name: 'itsmStatus', index: "itsm_status", width: 75, formatter: function (value) {
                    if (value === -1) {
                        return '<span>草稿</span>';
                    } else if (value === 1) {
                        return '<span>流转中</span>';
                    } else if (value === 2) {
                        return '<span>办结</span>';
                    } else if (value === 3) {
                        return '<span>作废</span>';
                    }

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
            storageName: null
        },

        showList: true,
        title: null,
        storageList: {},
        storage: {
            id: null,
            storageCode: null,
            storageName: null,
            storageArea: null,
            rentTime: null,
            staRentDate:null,
            endRentDate:null,
            rentPrice: null,
            renterId: null,
            renterPhone: null,
            landlordId: null,
            landlordPhone: null,
            storageDoornum: null
        },
        selected: '-1',
        selected2: '-1',
        UserDataList:[],

    },

    created() {
        this.getUserInfo();
    },
    mounted: function() {
        this.dateInit()
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
                postData: {'search': vm.q.storageName},  //postData是post请求参数Data
                page: page
            }).trigger("reloadGrid");
        },

        //重置
        reset: function () {
            window.location.href = baseURL + "modules/proc/procStorageApply.html";
        },

        //进入新增
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.storageList = {};
            vm.storage = {
                id: null,
                storageCode: null,
                storageName: null,
                storageArea: null,
                rentTime: null,
                staRentDate:null,
                endRentDate:null,
                rentPrice: null,
                renterName: null,
                renterId: null,
                renterPhone: null,
                landlordName: null,
                landlordId: null,
                landlordPhone: null,
                storageDoornum: null,
                storageAddress: null
            }
            vm.selected = '-1';
            vm.selected2 = '-1';
        },
        //时间控件初始化
        dateInit: function() {
            $('#startTime').datetimepicker({
                format: 'YYYY-MM-DD'
            })
            $('#endTime').datetimepicker({
                format: 'YYYY-MM-DD'
            })
        },

        //获得用户信息
        getUserInfo() {
            $.get(baseURL + "proc/procStorage/getUsersInfomation", function (r) {
               console.log(r);
             vm.UserDataList =   r.data;
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
            $.get(baseURL + "proc/procStorage/info/" + id, function (r) {
                console.log(r.data.storage);
                vm.storage = r.data.storage;
                vm.selected = r.data.storage.renterId;
                vm.selected2 = r.data.storage.landlordId;
                $('#endTime').val(r.data.storage.staRentDate);
                $('#startTime').val(r.data.storage.endRentDate);
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
                    url: baseURL + "proc/procStorage/delete",
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
        //获取用户信息
        changeRenter:function (userid){
            vm.storage.renterId= userid;
            $.get(baseURL + "sys/user/getSelUserinfo/" + userid, function (r) {
               vm.storage.renterPhone= r.data.user.mobile;
            });
        },
        //获取用户信息
        landlorder:function (userid){
            vm.storage.landlordId= userid;
            $.get(baseURL + "sys/user/getSelUserinfo/" + userid, function (r) {
                vm.storage.landlordPhone= r.data.user.mobile;
            });
        },

        //保存-更新数据
        saveOrUpdate: function () {
            var url = vm.storage.id == null ? "proc/procStorage/save" : "proc/procStorage/update";
            vm.storage.staRentDate = $('#startTime').val();
            vm.storage.endRentDate = $('#endTime').val();
            console.log(vm.storage)
            //isNumber校验是否为数字， common.js中
            if (!isNumber(vm.storage.storageArea)) {
                layer.alert("仓库面积应为数字！");
                return false;
            }
            if (!isNumber(vm.storage.rentTime)) {
                layer.alert("租期应为数字！");
                return false;
            }
            if (!isNumber(vm.storage.rentPrice)) {
                layer.alert("租金应为数字！");
                return false;
            }
            if (vm.selected == -1){
                layer.alert("请选择租户！");
                return false;
            }
            if (vm.selected2 == -1){
                layer.alert("请选择房东！");
                return false;
            }
            if ( $('#startTime').val() == ""){
                layer.alert("起租日期不能为空！");
                return false;
            }
            if ($('#endTime').val() == ""){
                layer.alert("到期日期不能为空！");
                return false;
            }

            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.storage),
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
        },
        //提交工作流申请
        applyStorage: function () {
            var id = 'aaaaaa';
            console.log(id);
            $.get(baseURL + "/proc/procStorage/apply?" + id, function (r) {
                console.log(r);
            });
        },
        flowView: function () {
            console.log("流程查看")
            var imgHtml = "<img src= 'https://images.gitbook.cn/d58fbc40-b33b-11e9-8857-a9cfb47d2814'/>";
            layer.open({
                type: 1,
                offset: '30px',
                skin: 'layui-layer-molv',
                title: "流程查看",
                area: ['800px', '600px'],
                shade: 0,
                shadeClose: false,
                content: imgHtml,
                btn: ['取消']
            });
        }


    }
});