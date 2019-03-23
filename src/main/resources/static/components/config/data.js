angular.module('app').factory('data', ['config', 'request',
    function (config, request) {
        var methods = {}
        //-----------hien thi history----------------------
        methods.history = function (data) {
            var url = config.HOST + '/history';

            return request.get(url,data);
        }

        methods.historyhits = function (data) {
            var url = config.HOST + '/historyhits';
            return request.get(url,data);
        }
        //---------------tim kiem history-----------------historyhits
        methods.sehistory = function (todoData) {
            var url = config.HOST + '/history';
            return request.get(url, todoData);
        }

        methods.sehistoryhits = function (todoData) {
            var url = config.HOST + '/historyhits';
            return request.post(url, todoData);
        }

        methods.login = function (todoData) {
            var url = config.HOST + '/login';
            return request.post(url, todoData);
        }
        methods.logout = function () {
            document.cookie = "";
            var url = config.HOST + '/login';
            return request.post(url);
        }

        // //-----------hien thi account---------------------
        // methods.getAcctno = function () {
        //     var url = config.HOST + '/buyNomarl';
        //     return request.get(url);
        // }
        //-----------hien thi role------------------------------------
        methods.getIsstaft = function () {
            var url = config.HOST + '/getIss';
            return request.get(url);
        }
        //-----------hien thi data tu database------------------------
        methods.getViewData = function (symbol) {
            var url = config.HOST + '/viewdata/' + symbol;
            return request.get(url);
        }
        //-----------Dat lenh mua------------------------------
        methods.createNormal = function (todoData) {
            var url = config.HOST + '/buyNomarl';
            return request.get(url, todoData);
        }
        //-----------Dat lenh ban------------------------------
        methods.createNormalBan = function (todoData) {
            var url = config.HOST + '/sellNomarl';
            return request.post(url, todoData);
        }
        //-----------Dat lenh ban------------------------------
        methods.createNormalBanUser = function (todoData) {
            var url = config.HOST + '/sellNomarlUser';
            return request.post(url, todoData);
        }
        //-----------Duyet nop-----------------------------
        methods.duyetNop = function (todoData) {
            var url = config.HOST + '/depositap';
            return request.post(url, todoData);
        }
        //-----------Duyet rut---------------------------------
        methods.duyetRut = function (todoData) {
            var url = config.HOST + '/withdrawalap';
            return request.post(url, todoData);
        }
        //----------Cap tai khoan-----------------------
        methods.captksm = function (todoData) {
            var url = config.HOST + '/captaikhoan';
            return request.post(url, todoData);
        }
        //----------Ban co phieu---------------------
        // methods.createNormalBan = function (todoData) {
        //     var url = config.HOST + '/normalBan';
        //     return request.post(url,todoData);
        // }
        //hien thi thong tin cac tai khoan can duyet nop
        methods.getDuyetNop = function () {
            var url = config.HOST + '/duyetnop';
            return request.get(url);
        }
        //--------------hien thi trang duyet tai khoan---------------------
        methods.getTK = function () {
            var url = config.HOST + '/gettk';
            return request.get(url);
        }

        methods.addSysVar = function (todo) {
            var url = config.HOST + '/addSysVar';
            return request.post(url, todo);
        }

        methods.saveUpdateSysVar = function (todo) {
            var url = config.HOST + '/updateSysVar';
            return request.put(url, todo);
        }

        methods.deleteSysVar = function (todo) {
            var url = config.HOST + '/deleteSysVar';
            console.log("ddsdsdsa",todo)
            return request.deletes(url, todo);
        }

        //hien thi trang duyet rut-------------------
        methods.getDuyetRut = function () {
            var url = config.HOST + '/duyetrut';
            return request.get(url);
        }
        //nop tien----------------------------------
        methods.noptien = function (todoData) {
            var url = config.HOST + '/deposit';
            return request.post(url, todoData);
        }
        //rut tien---------------------------------
        methods.ruttien = function (todoData) {
            var url = config.HOST + '/withdrawal';
            return request.post(url, todoData);
        }

        methods.cuoingay = function () {
            var url = config.HOST + '/endDate';
            return request.post(url);
        }

        //--hien thi trang xu ly tai khoan-----
        methods.luongchungse = function () {
            var url = config.HOST + '/luongchungse';
            return request.get(url);
        }

        methods.thamso = function () {
            var url = config.HOST + '/getSysVar';
            return request.get(url);
        }

        methods.chungkhoan = function () {
            var url = config.HOST + '/getthamsock';
            return request.get(url);
        }

        //--hien thi ho ten---------------------
        methods.getHoten = function (account) {
            var url = config.HOST + '/gethoten/' + account;
            return request.get(url);
        }
        //-----hien thi thong tin chung-----------------
        methods.ttchung = function () {
            var url = config.HOST + '/ttchung';
            return request.get(url);
        }

        methods.historyAdmin = function () {
            var url = config.HOST + '/historyadmin';
            return request.get(url);
        }

        methods.randomAccount = function () {
            var url = config.HOST + '/custid';
            return request.get(url);
        }

        //----------------------ty le-------------------------
        methods.tttyle = function () {
            var url = config.HOST + '/tttyle';
            return request.get(url);
        }
        //----huy dat lenh-----------------------
        methods.deletes = function (id) {
            var url = config.HOST + '/huy/' + id;
            return request.deletes(url);
        }

        methods.hisxulytaikhoan = function (id) {
            var url = config.HOST + '/historyView/' + id;
            return request.get(url);
        }

        //-------hien thi ten san-------------------------
        methods.floorName = function (symbol) {
            var url = config.HOST + '/floorname/' + symbol;
            return request.get(url);
        }
        //------------hien thi gia------------------------
        methods.priceView = function (symbol) {
            var url = config.HOST + '/price/' + symbol;
            return request.get(url);
        }

        return methods;
    }
])