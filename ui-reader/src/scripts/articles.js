export function initInfiniteScroll() {
  const $ = (s) => document.querySelector(s);
  const wrapper = document.querySelector('.blog-wrapper');
  const sentinel = $('#scroll-sentinel');
  const loader = $('#loader');
  const retry = $('#retry');

  if (wrapper && sentinel && loader && retry) {
    let page = 1, busy = false, more = true;
    const size = Number(wrapper.dataset.pageSize || '12');
    const base = wrapper.dataset.apiBase || '';
    const catMap = JSON.parse(wrapper.dataset.catMap || '{}');

    const show = (el, v) => el.classList.toggle('hidden', !v);

    const categoryColors = {
      'Java':            { bg: '#fff3e0', text: '#e65100', border: '#ffcc80' },
      'Spring Boot':     { bg: '#e8f5e9', text: '#2e7d32', border: '#a5d6a7' },
      'Kafka':           { bg: '#fce4ec', text: '#c62828', border: '#f48fb1' },
      'DSA':             { bg: '#e3f2fd', text: '#1565c0', border: '#90caf9' },
      'Design Patterns': { bg: '#f0effe', text: '#6d5bd0', border: '#ddd9fc' },
    };

    const card = (a) => {
      const el = document.createElement('a');
      el.href = `/article/${a.id}`;
      el.className = 'article-card small';

      const thumb = a.thumbnail || `https://picsum.photos/seed/${a.id}/800/500`;
      const plainText = a.content ? a.content.replace(/<[^>]*>/g, '') : '';
      const wordCount = plainText.split(/\s+/).filter(Boolean).length;
      const readTime = Math.max(1, Math.round(wordCount / 200));
      const categoryName = catMap[a.categoryId] || a.categoryName || '';
      const color = categoryColors[categoryName] || { bg: '#f3f4f6', text: '#374151', border: '#e5e7eb' };

      el.innerHTML = `
        <div class="card-img">
          <img src="${thumb}" alt="${a.title}" loading="lazy" />
        </div>
        <div class="card-info">
          ${categoryName ? `<span class="category" style="background:${color.bg}; color:${color.text}; border-color:${color.border};">${categoryName}</span>` : ''}
          <h2 class="card-title">${a.title}</h2>
          <span class="meta">${readTime} min read</span>
        </div>`;
      return el;
    };

    const load = async () => {
      if (busy || !more) return;
      busy = true;
      show(loader, true);
      show(retry, false);
      try {
        const r = await fetch(`${base}/article?size=${size}&page=${page}`);
        const { articleSummaryList: list = [] } = await r.json();

        let extraGrid = document.getElementById('extra-grid');
        if (!extraGrid) {
          extraGrid = document.createElement('div');
          extraGrid.id = 'extra-grid';
          extraGrid.style.cssText = 'display:grid; grid-template-columns:repeat(auto-fill, minmax(380px, 1fr)); gap:20px; margin-top:20px;';
          document.getElementById('articles-grid')?.appendChild(extraGrid);
        }

        list.forEach((a) => extraGrid.appendChild(card(a)));
        more = list.length === size;
        page += more ? 1 : 0;
        if (!more) obs.disconnect();
      } catch (e) {
        console.error(e);
        show(retry, true);
      } finally {
        busy = false;
        show(loader, false);
      }
    };

    retry.querySelector('button').addEventListener('click', load);

    const obs = new IntersectionObserver(
      (es) => es.some((e) => e.isIntersecting) && load(),
      { rootMargin: '200px 0px' }
    );
    obs.observe(sentinel);
  }
}