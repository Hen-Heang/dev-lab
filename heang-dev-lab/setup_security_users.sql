-- =====================================================
-- Spring Security Test Users Setup Script
-- 스프링 시큐리티 테스트 사용자 설정 스크립트
-- =====================================================
-- Run this script after application starts to create test users
-- 애플리케이션 시작 후 테스트 사용자를 생성하려면 이 스크립트를 실행하세요.

-- If role column doesn't exist, add it first
-- role 컬럼이 없으면 먼저 추가하세요
-- ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'ROLE_USER';

-- Delete existing test users (기존 테스트 사용자 삭제)
DELETE FROM users WHERE username IN ('admin', 'johndoe', 'janedoe', 'inactive_user', 'testuser');

-- =====================================================
-- BCrypt Password Information (BCrypt 비밀번호 정보)
-- =====================================================
-- All test users have password: password123
-- 모든 테스트 사용자의 비밀번호: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.

-- =====================================================
-- Create Test Users (테스트 사용자 생성)
-- =====================================================

-- Admin user (관리자) - Role: ROLE_ADMIN
-- Username: admin / Password: password123
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES (
    'admin',
    'admin@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
    'Admin User',
    '010-0000-0000',
    'ROLE_ADMIN',
    'ACTIVE'
);

-- Normal user 1 (일반 사용자 1) - Role: ROLE_USER
-- Username: johndoe / Password: password123
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES (
    'johndoe',
    'john@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
    'John Doe',
    '010-1234-5678',
    'ROLE_USER',
    'ACTIVE'
);

-- Normal user 2 (일반 사용자 2) - Role: ROLE_USER
-- Username: janedoe / Password: password123
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES (
    'janedoe',
    'jane@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
    'Jane Doe',
    '010-9876-5432',
    'ROLE_USER',
    'ACTIVE'
);

-- Inactive user (비활성 사용자) - for testing disabled account login
-- Username: inactive_user / Password: password123
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES (
    'inactive_user',
    'inactive@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
    'Inactive User',
    '010-1111-1111',
    'ROLE_USER',
    'INACTIVE'
);

-- =====================================================
-- Verify Users (사용자 확인)
-- =====================================================
SELECT id, username, email, name, role, status, created_at
FROM users
ORDER BY id;

-- =====================================================
-- Test Login Credentials (테스트 로그인 정보)
-- =====================================================
-- | Username       | Password     | Role       | Status   |
-- |----------------|--------------|------------|----------|
-- | admin          | password123  | ROLE_ADMIN | ACTIVE   |
-- | johndoe        | password123  | ROLE_USER  | ACTIVE   |
-- | janedoe        | password123  | ROLE_USER  | ACTIVE   |
-- | inactive_user  | password123  | ROLE_USER  | INACTIVE |
-- =====================================================
