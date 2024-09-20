document.getElementById('uploadForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const pdfFile = document.getElementById('pdfFile').files[0];
    if (!pdfFile) {
        alert('Please select a PDF file.');
        return;
    }

    const formData = new FormData();
    formData.append('pdfFile', pdfFile);

    fetch('/api/embedding/upload-pdf', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            const statusDiv = document.getElementById('status');
            if (data.success) {
                statusDiv.innerHTML = `<p>Upload successful! ${data.message}</p>`;
            } else {
                statusDiv.innerHTML = `<p>Error: ${data.message}</p>`;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('status').innerHTML = '<p>Upload failed!</p>';
        });
});
