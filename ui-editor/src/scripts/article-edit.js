const API_BASE = document.body.dataset.apiBase;

document.addEventListener("DOMContentLoaded", () => {
  const prismScript = document.createElement("script");
  prismScript.src = "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/prism.min.js";
  const prismJsScript = document.createElement("script");
  prismJsScript.src = "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-javascript.min.js";

  prismScript.onload = () => {
    document.body.appendChild(prismJsScript);
  };

  const script = document.createElement("script");
  script.src = "https://cdn.quilljs.com/1.3.6/quill.js";
  script.onload = async () => {
    document.body.appendChild(prismScript);

    const quill = new Quill("#editor-container", {
      theme: "snow",
      modules: {
        toolbar: [
          ["bold", "italic", "underline", "strike"],
          ["blockquote", "code-block"],
          [{ header: 1 }, { header: 2 }],
          [{ list: "ordered" }, { list: "bullet" }],
          [{ script: "sub" }, { script: "super" }],
          [{ indent: "-1" }, { indent: "+1" }],
          [{ direction: "rtl" }],
          [{ size: ["small", false, "large", "huge"] }],
          [{ header: [1, 2, 3, 4, 5, 6, false] }],
          [{ color: [] }, { background: [] }],
          [{ font: [] }],
          [{ align: [] }],
          ["clean"],
          ["link", "image", "video"]
        ]
      }
    });

    function highlightCodeBlocks() {
      if (!window.Prism) return;
      const codeBlocks = quill.root.querySelectorAll('pre.ql-syntax');
      codeBlocks.forEach(block => {
        if (!block.classList.contains('prism-highlighted')) {
          block.classList.add('prism-highlighted');
          const code = block.textContent || '';
          const codeElement = document.createElement('code');
          codeElement.className = `language-javascript`;
          codeElement.textContent = code;
          block.innerHTML = '';
          block.appendChild(codeElement);
          Prism.highlightElement(codeElement);
        }
      });
    }

    let highlightTimeout;
    quill.on('text-change', () => {
      clearTimeout(highlightTimeout);
      highlightTimeout = setTimeout(() => highlightCodeBlocks(), 300);
    });

    const checkPrism = setInterval(() => {
      if (window.Prism) {
        clearInterval(checkPrism);
        setTimeout(highlightCodeBlocks, 100);
      }
    }, 100);

    const btn = document.getElementById("btnSave");
    const titleInput = document.getElementById("articleTitle");
    const categorySelect = document.getElementById("articleCategory");
    const thumbnailInput = document.getElementById("articleThumbnail");
    const thumbnailPreview = document.getElementById("thumbnailPreview");
    const thumbnailImg = document.getElementById("thumbnailImg");
    const thumbnailStatus = document.getElementById("thumbnailStatus");
    const container = document.querySelector(".main-content");
    const articleId = container.dataset.articleId;

    // Thumbnail önizleme
    thumbnailInput.addEventListener("change", () => {
      const file = thumbnailInput.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          thumbnailImg.src = e.target.result;
          thumbnailPreview.style.display = "block";
          thumbnailStatus.textContent = `Selected: ${file.name}`;
        };
        reader.readAsDataURL(file);
      }
    });

    btn.addEventListener("click", async () => {
      const title = titleInput.value.trim();
      const categoryId = categorySelect.value;
      const content = quill.root.innerHTML;

      if (!title || !categoryId || !content) {
        alert("All fields are required!");
        return;
      }

      let thumbnailUrl = null;

      // Yeni görsel seçildiyse S3'e yükle
      const file = thumbnailInput.files[0];
      if (file) {
        try {
          thumbnailStatus.textContent = "Uploading image...";
          const formData = new FormData();
          formData.append("file", file);

          const uploadRes = await fetch(`https://hikmetcakir.com/api/upload/thumbnail`, {
            method: "POST",
            credentials: "include",
            body: formData
          });

          if (!uploadRes.ok) throw new Error("Image upload failed");
          const uploadData = await uploadRes.json();
          thumbnailUrl = uploadData.url;
          thumbnailStatus.textContent = "Image uploaded successfully!";
        } catch (err) {
          alert("Image upload failed: " + err.message);
          return;
        }
      }

      try {
        const body = {
          title,
          content,
          categoryId,
          updatedBy: "admin"
        };

        if (thumbnailUrl) {
          body.thumbnail = thumbnailUrl;
        }

        const res = await fetch(`https://hikmetcakir.com/article/${articleId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify(body)
        });

        if (!res.ok) throw new Error(await res.text());

        alert("Article updated successfully!");
        window.location.href = "/admin/article";
      } catch (err) {
        alert("Error: " + err.message);
      }
    });
  };

  document.body.appendChild(script);
});