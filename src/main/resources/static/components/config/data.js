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

        methods.userInfo = function () {
            var url = config.HOST + '/users/info';
            return request.get(url);
        }

        methods.accNames = function () {
            var url = config.HOST + '/getTypeNames';
            return request.get(url);
        }

        methods.userInfoUpdate = function (data) {
            var url = config.HOST + '/users/update';
            return request.put(url, data);
        }

        methods.changePass = function (data) {
            var url = config.HOST + '/doimk';
            return request.put(url, data);
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
            return request.put(url, todoData);
        }
        //-----------Duyet rut---------------------------------
        methods.duyetRut = function (todoData) {
            var url = config.HOST + '/withdrawalap';
            return request.put(url, todoData);
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

            return request.get(url, todo);
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
            var url = config.HOST + '/getSecurity';
            return request.get(url);
        }

        methods.addSecurity = function (data) {
            var url = config.HOST + '/addSecurity';
            return request.post(url, data);
        }

        methods.saveUpdateSecurity = function (data) {
            var url = config.HOST + '/updateSecurity';
            return request.put(url, data);
        }

        methods.deleteSecurity = function (data) {
            var url = config.HOST + '/deleteSecurity';
            return request.get(url, data);
        }

        methods.afType = function () {
            var url = config.HOST + '/getAfTypes';
            return request.get(url);
        }

        methods.addAfType = function (data) {
            var url = config.HOST + '/addAfType';
            return request.post(url, data);
        }

        methods.saveUpdateAfType = function (data) {
            var url = config.HOST + '/updateAfType';
            return request.put(url, data);
        }

        methods.deleteAfType = function (data) {
            var url = config.HOST + '/deleteAfType';
            return request.get(url, data);
        }

        //--hien thi ho ten---------------------
        methods.getHoten = function (custId) {
            var url = config.HOST + '/users/name';
            return request.get(url, custId);
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