<template>
  <footer class="footer">
    <div class="container">
      <div class="row">
        <div class="col-md-4 mb-3">
          <h5>电商购物平台</h5>
          <p class="small">一站式在线购物体验，海量商品任您挑选。</p>
        </div>
        <div class="col-md-4 mb-3">
          <h5>快速链接</h5>
          <ul class="list-unstyled small">
            <li><router-link to="/" class="text-muted">首页</router-link></li>
            <li><router-link to="/products" class="text-muted">全部商品</router-link></li>
            <li><router-link to="/cart" class="text-muted">购物车</router-link></li>
          </ul>
        </div>
        <div class="col-md-4 mb-3">
          <h5>联系我们</h5>
          <p class="small text-muted">
            <i class="bi bi-envelope"></i> support@ecommerce.com<br />
            <i class="bi bi-telephone"></i> 400-123-4567
          </p>
        </div>
      </div>
      <hr class="bg-secondary" />
      <p class="text-center small mb-0">
        &copy; 2026 电商购物平台. All rights reserved. Powered by Spring Boot + Vue 3
      </p>
    </div>
  </footer>
</template>
