(function () {

    const uploadFile = document.getElementById('uploadFile');

    if (uploadFile.files.length) {
        upload();
    }

    uploadFile.addEventListener('change', upload, false);


    function upload(e) {
        const file = uploadFile.files[0];
        const _1K = 1024;
        const chunkSize = 1024 * _1K;
        const chunks = Math.ceil(file.size / chunkSize);

        const date = new Date();
        const timestamp = date.getTime();

        for (let i = 0; i < chunks; i++) {
            (function (i) {
                const isLast = (i == chunks - 1);
                const start = i * chunkSize;
                const end = isLast ? file.size : (i + 1) * chunkSize;
                const chunk = file.slice(start, end, file.type);


                const metaData = {
                    "fileName": file.name,
                    "fileSize": file.size,
                    "chunkSize": chunkSize,
                    "start": start,
                    "end": end,
                    "md5": timestamp,
                    "nth": i + 1,
                    "chunks": chunks,
                };
                send(metaData, chunk);
            })(i);
        }

    }

    function send(metaData, chunk) {
        const oReq = new XMLHttpRequest();
        let queryString = "";
        for (let key in metaData) {
            queryString += encodeURIComponent(key) + "=" + encodeURIComponent(metaData[key]) + "&";
        }
        oReq.open("POST", '/upload?' + queryString, true);
        oReq.onload = function (oEvent) {
            console.info(oEvent);
        };
        oReq.send(chunk);
    }

})();