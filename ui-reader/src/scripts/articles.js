export function initInfiniteScroll() {
   const $ = (s) => document.querySelector(s);
    const grid = $('#articles-grid');
    const sentinel = $('#scroll-sentinel');
    const loader = $('#loader');
    const retry = $('#retry');
    
    if (grid && sentinel && loader && retry) {
      let page = 1, busy = false, more = true;
      const size = Number(grid.dataset.pageSize || '12');
      const base = grid.dataset.apiBase || '';

      const show = (el, v) => el.classList.toggle('hidden', !v);
      const trunc = (t, n = 180) => t ? (t.length > n ? t.slice(0, n) + '...' : t) : '';

      const card = (a) => {
        const el = document.createElement('article');
        el.className = 'bg-white rounded-3xl border border-gray-100 shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden w-full h-full flex flex-col';
        el.innerHTML = `
          <div class="w-full h-56 overflow-hidden">
            <img src="${a.thumbnail || `https://picsum.photos/seed/${a.id}/600/400`}" alt="${a.title}" class="w-full h-full object-cover hover:scale-105 transition-transform duration-500" loading="lazy" />
          </div>
          <div class="p-6 flex flex-col flex-1">
            <div>
              <h2 class="text-2xl font-semibold mb-3 text-gray-900 line-clamp-2">${a.title}</h2>
              <p class="text-gray-700 leading-relaxed mb-6">${trunc(a.content)}</p>
            </div>
            <div class="mt-auto flex justify-end">
              <a href="/article/${a.id}" class="px-5 py-2 border border-black text-black rounded-full hover:bg-black hover:text-white transition-colors duration-300">Read More</a>
            </div>
          </div>`;
        return el;
      };

      const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
      const TEST_DELAY_MS = 800;

      const load = async () => {
        if (busy || !more) return; busy = true; show(loader, true); show(retry, false);
        try {
          const r = await fetch(`${base}/article?size=${size}&page=${page}`);
          await sleep(TEST_DELAY_MS); 
          const { articleSummaryList: list = [] } = await r.json();
          list.forEach((a) => grid.appendChild(card(a)));
          more = list.length === size; page += more ? 1 : 0; if (!more) obs.disconnect();
        } catch (e) {
          console.error(e); show(retry, true);
        } finally {
          busy = false; show(loader, false);
        }
      };
      retry.querySelector('button').addEventListener('click', load);

      const obs = new IntersectionObserver((es) => es.some((e) => e.isIntersecting) && load(), { rootMargin: '200px 0px' });
      obs.observe(sentinel);
    }
}