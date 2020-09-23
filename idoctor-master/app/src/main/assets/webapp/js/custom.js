/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

webApp.controller("PexipCtrl", function ($scope, $apply, $timeout, $storage) {
    /* Js var */
    var btnEndCall; // Nút disconnect
    var btnSwitchCamera; // Nút chuyển camera
    var boxBooking; // Nút chuyển camera
    var chrome_ver = 0;
    var safari_ver = 0;
    var firefox_ver = 0;
    var edge_ver = 0;

    /* Pexip var */
    $scope.setting = {
        'callParams': {
            'node': "vr-conf-1.dynu.com", // Địa chỉ server conference
            'pin': "123456", // Mật khẩu truye cập phòng
            'conference': "1900", // Tên phòng để join
            'name': "Tham", // Tên hiển thị
            'bandwidth': 1280, // Băng thông
            'callType': "",
            'flash': ""
        },
        'media': {
            "audioSource": null, // Nguồn audio mặc định
            "videoSource": null, // Nguồn video mặc định
            "bandwidthIn": 576, // băng thông vào, mặc định
            "bandwidthOut": 576 // băng thông ra, mặc định
        }
    }

    $scope.filter = {
        "status": "connecting"
    };

    /* WebRTC var */
    var rtc = null; // pexip var 
    var video; // my camera
    var videoConference; // camera conferencing

    navigator.getUserMedia = (navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.msGetUserMedia); // webrtc var

    /**
     * DS nguồn video, audio, output
     */
    $scope.mediaSources = {
        audio: [],
        video: [],
        output: []
    };

    /* PRTC function */
    /**
     * Xử lý khi window beforeunload
     * disconnect pertc
     * stop all stream
     * @param {type} event
     * @returns {undefined}
     */
    function finalise(event) {
        rtc.disconnect();
        rtc = null;
        $scope.stopAllStream();
    }

    /**
     * Xử lý khi mất kết nối đến server
     * @param {type} reason
     * @returns {undefined}
     */
    function remoteDisconnect(reason) {
        $scope.stopAllStream();
        alert(reason);
        window.removeEventListener('beforeunload', finalise);
//        window.close();
    }

    /**
     * main function
     * Xử lý khi load xong server(window.onload)
     * @returns {undefined}
     */
    function initialise() {
        video = document.getElementById("video-camera");
        videoConference = document.getElementById("video-conference");
        btnEndCall = document.getElementById("btn-end-call");
        btnSwitchCamera = document.getElementById("btn-switch-camera");
        boxBooking = document.getElementById("book");

        rtc = new PexRTC();

        rtc.onSetup = onDoneSetup;
        rtc.onConnect = onConnect;
        rtc.onDisconnect = remoteDisconnect;
        rtc.onError = onError;

        // start conference
        $scope.startConference();

        window.addEventListener('beforeunload', finalise);

        // Gán sự kiện Nút disconnect
        btnEndCall.addEventListener('click', function () {
            rtc.disconnect();
            $scope.stopAllStream();
            rtc = null;
            videoConference.style.opacity = 0;
            video.style.opacity = 0;
        });

        // gán sự kiện chuyển camera
        btnSwitchCamera.addEventListener('click', function () {
            $scope.switchCamera();
        });

        /**
         * Tính toán width,height video 
         * Set un muted video
         */
        videoConference.addEventListener('loadedmetadata', function () {
            calcAlignVideoConf();
            videoConference.style.opacity = 1;
        }, false);

        window.onresize = calcAlignVideoConf;
    }

    /**
     * Log lỗi
     * @param {type} error
     * @returns {undefined}
     */
    function onError(error) {
        console.log(error);
        alert(error);
    }

    /**
     * Xử lý khi makeCall thành công
     * @param {type} stream
     * @param {type} pin_status
     * @param {type} conference_extension
     * @returns {undefined}
     */
    function onDoneSetup(stream, pin_status, conference_extension) {
        gotStream(stream);
        var PIN = $scope.setting.callParams.pin;
        // xác thực vs mã PIN
        rtc.connect(PIN);
    }

    /**
     * Xử lý sau khi rtc.connect
     * @param {type} stream
     * @returns {undefined}
     */
    function onConnect(stream) {
        try {
            videoConference.srcObject = stream;
        } catch (error) {
            videoConference.src = window.URL.createObjectURL(stream);
        }
    }

    /**
     * Hien thi danh sach suorce video
     * @param {type} deviceInfos
     * @returns {undefined}
     */
    function gotDevices(deviceInfos) {
        if (!deviceInfos)
            return false;

        var videoDevices = [];
        var audioInput = [];
        var audioOutput = [];
        for (var i = 0; i !== deviceInfos.length; ++i) {
            var deviceInfo = deviceInfos[i];
            if (deviceInfo.kind === 'videoinput') {
                videoDevices.push(deviceInfo);
            }

            if (deviceInfo.kind === 'audioinput') {
                audioInput.push(deviceInfo);
            }
            if (deviceInfo.kind === 'audiooutput') {
                audioOutput.push(deviceInfo);
            }
        }

        if (videoDevices.length) {
            $apply(function () {
                $scope.mediaSources.video = videoDevices;
                $scope.mediaSources.audio = audioInput;
                $scope.mediaSources.output = audioOutput;
            });
        }
    }

    /**
     * Gán nguồn cho video conference
     * @param {type} stream
     * @returns {undefined}
     */
    function gotStream(stream) {
        if (video && video.srcObject) {
            stopStreamedVideo(video);
        }

        try {
            video.srcObject = stream;
        } catch (error) {
            video.src = window.URL.createObjectURL(stream);
        }
    }

    /**
     * Tính toán căn giữa video
     * @returns {Boolean}
     */
    function calcAlignVideoConf(elem) {
        let vWidth = videoConference.videoWidth;
        let vHeight = videoConference.videoHeight;

        let windowWidth = window.innerWidth;
        let windowHeigth = window.innerHeight;

        if (vWidth > windowWidth) {
            videoConference.style.width = windowWidth + "px";
        } else {
            videoConference.style.height = windowHeigth + "px";
        }

        vWidth = videoConference.offsetWidth;
        vHeight = videoConference.offsetHeight;
        videoConference.style.marginTop = "0px";

        if (vWidth == windowWidth && vHeight == windowHeigth)
            return true;

        if (vWidth == windowWidth) {
            let offset = Math.ceil((windowHeigth - vHeight) / 2);
            if (offset > 1) {
                videoConference.style.marginTop = offset + "px";
            }
        }
    }

    /**
     * Hủy hết các luồng của stream video
     * @param {type} videoElem
     * @returns {undefined}
     */
    function stopStreamedVideo(videoElem) {
        let stream = videoElem.srcObject;

        if (stream) {
            videoElem.pause();
            videoElem.src = null;
            videoElem.srcObject = null;

            if (stream.getTracks) {
                stream.getTracks().forEach(function (track) {
                    track.stop();
                });
            }
            if (stream.getVideoTracks) {
                stream.getVideoTracks().forEach(function (track) {
                    track.stop();
                });
            }

            if (stream.stop) {
                stream.stop();
            }

            delete stream;
        }
    }


    /* WebRTC function */

    /**
     * Lấy vị trí của thiết bị camera với deviceID
     * @param {type} deviceID
     * @returns {Number|@var;i}
     */
    $scope.getMediaPos = function (deviceID) {
        var pos = -1;
        if (!deviceID.length || !$scope.mediaSources.video.length)
            return pos;

        for (var i in $scope.mediaSources.video) {
            if ($scope.mediaSources.video[i].deviceId == deviceID) {
                pos = i;
                break;
            }
        }

        return pos;
    }

    /**
     * Hủy hết các luồng visdeo hiện đang chạy
     * @returns {undefined}
     */
    $scope.stopAllStream = function () {
        if (video && video.srcObject) {
            stopStreamedVideo(video)
        }

        if (videoConference && videoConference.srcObject) {
            stopStreamedVideo(videoConference);
        }
    }

    /**
     * Xử lý kết nối connect conference
     * @returns {Boolean}
     */
    $scope.startConference = function () {
        if (!rtc || rtc == null)
            return false;

        var SERVER = $scope.setting.callParams.node;
        var ROOM = $scope.setting.callParams.conference;
        var NAME = $scope.setting.callParams.name;
        var BANDWIDTH = $scope.setting.callParams.bandwidth;
        var CALLTYPE = $scope.setting.callParams.callType;
        var FLASH = $scope.setting.callParams.flash;

        // call sang server conference
        rtc.audio_source = $scope.setting.media.audioSource;
        rtc.video_source = $scope.setting.media.videoSource;
        rtc.bandwidth_in = $scope.setting.media.bandwidthIn;
        rtc.bandwidth_out = $scope.setting.media.bandwidthOut;

        rtc.makeCall(SERVER, ROOM, NAME, BANDWIDTH, CALLTYPE, FLASH);
    }

    /**
     * Xử lý chuyển camera
     * Chọn từng camera 1, hết camear thì quay về camera đầu
     * @returns {undefined}
     */
    $scope.switchCamera = function () {
        $apply(function () {
            var videoSource;
            try {
                video.srcObject.getTracks().forEach((track) => {
                    videoSource = track.getSettings().deviceId;
                });
            } catch (error) {

            }

            var pos = $scope.getMediaPos(videoSource);

            if (pos != -1) {
                var nextPos = parseInt(pos) + 1;
                if (!$scope.mediaSources.video[nextPos]) {
                    nextPos = 0;
                }

                var mediaSrc = $scope.mediaSources.video[nextPos];

                var constraints = {
                    audio: true,
                    video: {
                        deviceId: mediaSrc.deviceId
                    }
                };

                navigator.getWebcam = (navigator.getUserMedia || navigator.webKitGetUserMedia || navigator.moxGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia);

                if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                    navigator.mediaDevices.getUserMedia(constraints).
                            then(function (stream) {
                                gotStream(stream);
                                rtc.video_source = mediaSrc.deviceId;
                                rtc.audio_source = null;
                                rtc.renegotiate();
                            }).then(gotDevices).catch(onError);
                } else if (navigator.getWebcam) {
                    navigator.getWebcam(constraints, function (stream) {
                        gotStream(stream);
                        rtc.video_source = mediaSrc.deviceId;
                        rtc.audio_source = null;
                        rtc.renegotiate();
                    }, onError);
                }
            }
        });
    }

    $scope.renderClass = function () {
        var _class = "browser";
        if (navigator.userAgent.indexOf("Mobile") != -1) {
            _class = "mobile";
        }

        return _class;
    }

    /**
     * trả về browser
     * @returns {customL#7.$scope.detectBrowser.ret}
     */
    $scope.detectBrowser = function () {
        var ret = {
            'browser': '',
            'version': ''
        };

        if (navigator.userAgent.indexOf("Chrome") != -1) {
            chrome_ver = parseInt(window.navigator.appVersion.match(/Chrome\/(\d+)\./)[1], 10);
            ret.browser = "Chrome";
            ret.version = chrome_ver;
        }

        if (navigator.userAgent.indexOf("Firefox") != -1) {
            firefox_ver = parseInt(window.navigator.userAgent.match(/Firefox\/(\d+)\./)[1], 10);
            ret.browser = "Firefox";
            ret.version = firefox_ver;
        }

        if (navigator.userAgent.indexOf("Edge") != -1) {
            edge_ver = parseInt(window.navigator.userAgent.match(/Edge\/\d+\.(\d+)/)[1], 10);
            chrome_ver = 0;
            ret.browser = "Edge";
            ret.version = edge_ver;
        }

        if (self.chrome_ver == 0 && navigator.userAgent.indexOf("Safari") != -1) {
            safari_ver = parseInt(window.navigator.appVersion.match(/Safari\/(\d+)\./)[1], 10);
            ret.browser = "Safari";
            ret.version = safari_ver;
        }

        return ret;
    }

    $scope.call = function () {
        boxBooking.style.display = "none";
        videoConference.muted = false;
    }
    /**
     * Start
     * @returns {undefined}
     */
    window.onload = function () {
        $scope.stopAllStream();
        initialise();

        if (navigator.getUserMedia) {
            navigator.mediaDevices.enumerateDevices().then(gotDevices).catch(onError);
        }
    }
});

