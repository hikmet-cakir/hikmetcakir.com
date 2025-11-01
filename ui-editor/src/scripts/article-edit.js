document.addEventListener("DOMContentLoaded", () => {
    const script = document.createElement("script");
    script.src = "https://cdn.quilljs.com/1.3.6/quill.js";
    script.onload = async () => {
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

        const btn = document.getElementById("btnSave");
        const titleInput = document.getElementById("articleTitle");
        const categorySelect = document.getElementById("articleCategory");
        

        btn.addEventListener("click", async () => {
        const title = titleInput.value.trim();
        const categoryId = categorySelect.value;
        const content = quill.root.innerHTML; 
        const container = document.querySelector(".main-content"); 
        const articleId = container.dataset.categoryId;    

        if (!title || !categoryId || !content) {
            alert("All fields are required!");
            return;
        }

        try {
            const res = await fetch(`http://localhost:8080/article/${articleId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                title,
                content,
                categoryId,
                updatedBy: "admin"
            })
            });

            if (!res.ok) throw new Error(await res.text());

            alert("Article updated successfully!");
            window.location.href = "/article";
        } catch (err) {
            alert("Error: " + err.message);
        }
        });
    };

    document.body.appendChild(script);
});