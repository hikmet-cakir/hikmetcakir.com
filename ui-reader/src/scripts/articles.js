export function initInfiniteScroll() {
  const $ = (s) => document.querySelector(s);
  const gridWrapper = $('#articles-grid');
  const sentinel = $('#scroll-sentinel');
  const loader = $('#loader');
  const retry = $('#retry');

  if (gridWrapper && sentinel && loader && retry) {
    let page = 1, busy = false, more = true;
    const size = Number(gridWrapper.dataset.pageSize || '12');
    const base = gridWrapper.dataset.apiBase || '';

    const show = (el, v) => el.classList.toggle('hidden', !v);

    const card = (a) => {
      const el = document.createElement('a');
      el.href = `/article/${a.id}`;
      el.className = 'article-card small';
      const thumb = a.thumbnail || `https://picsum.photos/seed/${a.id}/800/500`;
      el.innerHTML = `
        <div class="card-img">
          <img src="${thumb}" alt="${a.title}" loading="lazy" />
        </div>
        <div class="card-info">
          ${a.categoryName ? `<span class="category">${a.categoryName}</span>` : ''}
          <h2 class="card-title">${a.title}</h2>
          <span class="meta">By Hikmet Cakir</span>
        </div>`;
      return el;
    };

    const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

    const load = async () => {
      if (busy || !more) return;
      busy = true;
      show(loader, true);
      show(retry, false);
      try {
        const r = await fetch(`${base}/article?size=${size}&page=${page}`);
        await sleep(800);
        const { articleSummaryList: list = [] } = await r.json();

        // Yeni cardları extra-grid'e ekle
        const extraGrid = document.getElementById('extra-grid');
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