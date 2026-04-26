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

  const categoryColors = {
    'Java':            { bg: 'rgba(255,243,224,0.92)', text: '#e65100', border: 'rgba(255,204,128,0.6)' },
    'Spring Boot':     { bg: 'rgba(232,245,233,0.92)', text: '#2e7d32', border: 'rgba(165,214,167,0.6)' },
    'Kafka':           { bg: 'rgba(252,228,236,0.92)', text: '#c62828', border: 'rgba(244,143,177,0.6)' },
    'DSA':             { bg: 'rgba(227,242,253,0.92)', text: '#1565c0', border: 'rgba(144,202,249,0.6)' },
    'Design Patterns': { bg: 'rgba(240,239,254,0.92)', text: '#6d5bd0', border: 'rgba(221,217,252,0.6)' },
  };

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