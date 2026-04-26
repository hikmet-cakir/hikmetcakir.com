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
      'Java':            { bg: 'rgba(255,243,224,0.92)', text: '#e65100', border: 'rgba(255,204,128,0.6)' },
      'Spring Boot':     { bg: 'rgba(232,245,233,0.92)', text: '#2e7d32', border: 'rgba(165,214,167,0.6)' },
      'Kafka':           { bg: 'rgba(252,228,236,0.92)', text: '#c62828', border: 'rgba(244,143,177,0.6)' },
      'DSA':             { bg: 'rgba(227,242,253,0.92)', text: '#1565c0', border: 'rgba(144,202,249,0.6)' },
      'Design Patterns': { bg: 'rgba(240,239,254,0.92)', text: '#6d5bd0', border: 'rgba(221,217,252,0.6)' },
    };

    const formatDate = (dateStr) => {
      if (!dateStr) return '';
      return new Date(dateStr).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
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
      const color = categoryColors[categoryName] || { bg: 'rgba(243,244,246,0.92)', text: '#374151', border: 'rgba(229,231,235,0.6)' };
      const formattedDate = formatDate(a.created);
      const metaStr = [formattedDate, `${readTime} min read`].filter(Boolean).join(' · ');

      el.innerHTML = `
        <div class="card-img-wrap">
          <img src="${thumb}" alt="${a.title}" loading="lazy" class="card-img" />
          ${categoryName ? `<span class="card-badge" style="background:${color.bg}; color:${color.text}; border-color:${color.border};">${categoryName}</span>` : ''}
          <div class="card-overlay">
            <h2 class="card-title">${a.title}</h2>
            <p class="card-meta">${metaStr}</p>
          </div>
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